package com.xw.controller;

import com.xw.pojo.bo.ShopCartBO;
import com.xw.utils.JSONResult;
import com.xw.utils.JsonUtils;
import com.xw.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/7/21
 */
@Api(value = "购物车",tags = {"用于购物车的相关接口"})
@RestController
@RequestMapping("/shopCart")
public class ShopCartController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ShopCartController.class);

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @RequestMapping("/add")
    public JSONResult add(
            @ApiParam(name = "userId", value = "用户 id", required = true) @RequestParam String userId,
            @ApiParam(name = "shopCartBO", value = "购物车对象", required = true) @RequestBody ShopCartBO shopCartBO,
            HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg(null);
        }
        logger.info("购物车数据为：" + shopCartBO);
        // 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到 redis 缓存
        // 需要判断当前购物车中是否包含已经存在的商品，如果存在则累加购买数量
        String shopCartStr = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        List<ShopCartBO> shopCartList;
        if (StringUtils.isNotBlank(shopCartStr)) {
            // redis 中已经有购物车了
            shopCartList = JsonUtils.jsonToList(shopCartStr, ShopCartBO.class);
            // 判断购物车中是否存在已有商品，如果有的话，counts 累加
            boolean isHaving = false;
            for (ShopCartBO cart : shopCartList) {
                if (cart.getSpecId().equals(shopCartBO.getSpecId())) {
                    cart.setBuyCounts(cart.getBuyCounts() + shopCartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopCartList.add(shopCartBO);
            }
        } else {
            // redis中没有购物车，直接添加到购物车中
            shopCartList = new ArrayList<>();
            shopCartList.add(shopCartBO);
        }
        // 覆盖现有redis中的购物车
        redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopCartList));
        return JSONResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @RequestMapping("/delete")
    public JSONResult delete(
            @ApiParam(name = "userId", value = "用户 id", required = true) @RequestParam String userId,
            @ApiParam(name = "itemSpecId", value = "商品规格 id", required = true) @RequestParam String itemSpecId,
            HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JSONResult.errorMsg("参数不能为空");
        }
        // 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端 redis 购物车中的商品
        String shopCartStr = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(shopCartStr)) {
            // redis中已经有购物车了
            List<ShopCartBO> shopCartList = JsonUtils.jsonToList(shopCartStr, ShopCartBO.class);
            // 判断购物车中是否存在已有商品，如果有的话则删除
            for (ShopCartBO cart : shopCartList) {
                String tmpSpecId = cart.getSpecId();
                if (tmpSpecId.equals(itemSpecId)) {
                    shopCartList.remove(cart);
                    break;
                }
            }
            // 覆盖现有redis中的购物车
            redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopCartList));
        }
        return JSONResult.ok();
    }
}
