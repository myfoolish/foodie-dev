package com.xw.controller;

import com.xw.pojo.Items;
import com.xw.pojo.ItemsImg;
import com.xw.pojo.ItemsParam;
import com.xw.pojo.ItemsSpec;
import com.xw.pojo.vo.CommentLevelCountsVO;
import com.xw.pojo.vo.ItemsInfoVO;
import com.xw.pojo.vo.ShopCartVO;
import com.xw.service.ItemsService;
import com.xw.utils.JSONResult;
import com.xw.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/12/31
 */
@Api(value = "商品信息",tags = {"用于商品展示的相关接口"})
@RestController
@RequestMapping("/items")
public class ItemsController extends BaseController {

    @Autowired
    private ItemsService itemsService;

    @ApiOperation(value = "获取商品详情", notes = "获取商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JSONResult info(
            @ApiParam(name = "itemId", value = "商品 id", required = true) @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JSONResult.errorMsg(null);
        }
        Items items = itemsService.queryItemsById(itemId);
        List<ItemsImg> itemsImgList = itemsService.queryItemsImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemsService.queryItemsSpecList(itemId);
        ItemsParam itemsParam = itemsService.queryItemsParam(itemId);
        ItemsInfoVO itemsInfo = new ItemsInfoVO();
        itemsInfo.setItem(items);
        itemsInfo.setItemImgList(itemsImgList);
        itemsInfo.setItemSpecList(itemsSpecList);
        itemsInfo.setItemParams(itemsParam);
        return JSONResult.ok(itemsInfo);
    }

    @ApiOperation(value = "查询商品等级", notes = "查询商品等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品 id", required = true) @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JSONResult.errorMsg(null);
        }
        CommentLevelCountsVO countsVO = itemsService.queryCommentCounts(itemId);
        return JSONResult.ok(countsVO);
    }

    @ApiOperation(value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public JSONResult comments(
            @ApiParam(name = "itemId", value = "商品 id", required = true) @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = false) @RequestParam Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false) @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(itemId)) {
            return JSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult grid = itemsService.queryPagedItemsComments(itemId, level, page, pageSize);
        return JSONResult.ok(grid);
    }

    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public JSONResult search(
            @ApiParam(name = "keywords", value = "关键字", required = true) @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序", required = false) @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false) @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return JSONResult.errorMsg("搜索内容不能为空");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult grid = itemsService.searchItems(keywords, sort, page, pageSize);
        return JSONResult.ok(grid);
    }

    @ApiOperation(value = "通过三级分类 ID 搜索商品列表", notes = "通过三级分类 ID 搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public JSONResult catItems(
            @ApiParam(name = "catId", value = "三级分类 id", required = true) @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序", required = false) @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false) @RequestParam Integer pageSize) {
        if (catId == null) {
            return JSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult grid = itemsService.searchItemsByThirdCategoryId(catId, sort, page, pageSize);
        return JSONResult.ok(grid);
    }

    // 用于用户长时间未登录网站，刷新购物车中的数据（主要是根据价格）类似京东淘宝
    @ApiOperation(value = "通过商品规格 ids 查询最新的购物车商品数据", notes = "通过商品规格 ids 查询最新的购物车商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public JSONResult refresh(
            @ApiParam(name = "itemSpecIds", value = "拼接的商品规格 ids", required = true, example = "1001,1003,1006") @RequestParam String itemSpecIds) {
        if (StringUtils.isBlank(itemSpecIds)) {
            return JSONResult.ok();
        }
        List<ShopCartVO> list = itemsService.queryItemsBySpecIds(itemSpecIds);
        return JSONResult.ok(list);
    }
}
