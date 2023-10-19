package com.xw.controller;

import com.xw.enums.YesOrNo;
import com.xw.pojo.Carousel;
import com.xw.pojo.Category;
import com.xw.pojo.vo.CategoryVO;
import com.xw.pojo.vo.ItemsVO;
import com.xw.service.CarouselService;
import com.xw.service.CategoryService;
import com.xw.utils.JSONResult;
import com.xw.utils.JsonUtils;
import com.xw.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/12/31
 */
@Api(value = "首页",tags = {"用于首页展示的相关接口"})
@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel() {
        List<Carousel> result;
        String carouselStr = redisOperator.get("carousel");
        if (StringUtils.isBlank(carouselStr)) {
            result = carouselService.queryAll(YesOrNo.YES.type);
            // 优化   存入 redis 缓存
            redisOperator.set("carousel", JsonUtils.objectToJson(result));
        } else {
            result = JsonUtils.jsonToList(carouselStr, Carousel.class);
        }
        return JSONResult.ok(result);
    }
    /**
     * 缓存更改解决思路：
     * 1. 后台运营系统，一旦广告（轮播图）发生更改，就可以删除缓存，然后重置
     * 2. 定时重置，比如每天凌晨三点重置
     * 3. 每个轮播图都有可能是一个广告，每个广告都会有一个过期时间，过期了，再重置
     */

    /**
     * 首页分类展示需求
     * 1、第一次刷新主页查询大分类，渲染展示到首页
     * 2、如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
    @ApiOperation(value = "获取商品分类（一级分类）", notes = "获取商品分类（一级分类）", httpMethod = "GET")
    @GetMapping("/category")
    public JSONResult category() {
        List<Category> result;
        String categoryStr = redisOperator.get("category");
        if (StringUtils.isBlank(categoryStr)) {
            result = categoryService.queryRootCategory();
            redisOperator.set("category", JsonUtils.objectToJson(result));
        } else {
            result = JsonUtils.jsonToList(categoryStr, Category.class);
        }
        return JSONResult.ok(result);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JSONResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类 id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return JSONResult.errorMsg("分类不存在");
        }
        List<CategoryVO> result;
        String subCatStr = redisOperator.get("subCat:" + rootCatId);
        if (StringUtils.isBlank(subCatStr)) {
            result = categoryService.querySubCategory(rootCatId);
            /**
             * 查询的key在redis中不存在，对应的id在数据库中也不存在，
             * 此时被非法用户进行攻击，大量的请求会直接打在数据库上，造成宕机从而影响整个系统，这种现象称之为缓存穿透
             * 解决方案：把空的数据也缓存起来，比如空字符串、空对象、空数组和空list
             */
            if (result != null && result.size() > 0) {
                redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(result));
            } else {
                redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(result), 5 * 60);
            }
            // *** 也可不要上面的判断直接写
//            redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(result));   // 解决缓存穿透的简单解决方案
//            redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(result), 5 * 60);   // 解决缓存穿透的简单解决方案
        } else {
            result = JsonUtils.jsonToList(subCatStr, CategoryVO.class);
        }
        return JSONResult.ok(result);
    }

    @ApiOperation(value = "查询首页每个一级分类下的6条最新商品数据", notes = "查询首页每个一级分类下的6条最新商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类 id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return JSONResult.errorMsg("分类不存在");
        }
        List<ItemsVO> result = categoryService.querySixNewItems(rootCatId);
        return JSONResult.ok(result);
    }
}
