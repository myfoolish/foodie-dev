package com.xw.controller;

import com.xw.pojo.Users;
import com.xw.pojo.bo.ShopCartBO;
import com.xw.pojo.bo.UserBO;
import com.xw.pojo.vo.UsersVO;
import com.xw.service.UserService;
import com.xw.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/8/17
 */
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("/passport")
public class PassportController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "判断用户名是否存在", notes = "判断用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JSONResult usernameIsExist(@RequestParam String username) {
        // 1、判断用户名不能为空
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorMsg("用户名不能为空");
        }
        // 2、查找注册对用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已存在");
        }
        // 3、请求成功，用户名没有重复   HttpStatus
        return JSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody UserBO userBO,
                             HttpServletRequest request, HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();
        // 1、判断用户名和密码不能为空
        if (StringUtils.isBlank(username)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(confirmPwd)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }
        // 2、查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已存在");
        }
        // 3、判断密码长度不能少于6位
        if (password.length() < 6) {
            return JSONResult.errorMsg("密码长度不能少于6位");
        }
        // 4、判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            return JSONResult.errorMsg("两次密码输入不一致");
        }
        // 5、实现注册
        Users userResult = userService.createUser(userBO);
//        userResult = setNullProperty(userResult);   // 现在新增加一个 UsersVO，这个就再需要了

        // 实现用户的 redis 会话
        UsersVO usersVO = conventUsersVO(userResult);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true); // isEncode 是否加密

        // 同步购物车数据
        synchShopCartData(userResult.getId(), request, response);
        return JSONResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JSONResult login(@RequestBody UserBO userBO,
                            HttpServletRequest request, HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        // 1、判断用户名和密码不能为空
        if (StringUtils.isBlank(username)
                || StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }
        // 2、实现登录
        Users userResult = null;
        try {
            userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
            if (userResult == null) {
                return JSONResult.errorMsg("用户名或密码不正确");
            }
//            userResult = setNullProperty(userResult);

            // 实现用户的 redis 会话
            UsersVO usersVO = conventUsersVO(userResult);

            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true); // isEncode 是否加密

            // 同步购物车数据
            synchShopCartData(userResult.getId(), request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONResult.ok(userResult);
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setBirthday(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        return userResult;
    }

    @ApiOperation(value = "退出登录", notes = "退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JSONResult logout(@RequestParam String userId,
                             HttpServletRequest request, HttpServletResponse response) {

        // 清除用户相关信息的 cookie
        CookieUtils.deleteCookie(request, response, "user");

        // 分布式会话中需要清除用户数据
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);

        // 用户退出登录,清除 redis 中 user 的会话信息
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);

        return JSONResult.ok();
    }

    /**
     * 注册登录成功后，同步cookie和redis中的购物车数据
     * @param userId
     * @param request
     * @param response
     */
    private void synchShopCartData(String userId, HttpServletRequest request, HttpServletResponse response) {

        /**
         * 1. redis中无数据，如果cookie中的购物车为空，那么这个时候不做任何处理
         *                 如果cookie中的购物车不为空，此时直接放入redis中
         * 2. redis中有数据，如果cookie中的购物车为空，那么直接把redis的购物车覆盖本地cookie
         *                 如果cookie中的购物车不为空，
         *                  如果cookie中的某个商品在redis中存在，则以cookie为主，删除redis中的，把cookie中的商品直接覆盖redis中（参考京东）
         * 3. 同步到redis中去了以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步最新的
         */

        // 从redis中获取购物车
        String shopCartStrRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        // 从cookie中获取购物车
        String shopCartStrCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);
        if (StringUtils.isBlank(shopCartStrRedis)) {
            // redis为空，cookie不为空，直接把cookie中的数据放入redis
            if (StringUtils.isNotBlank(shopCartStrCookie)) {
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopCartStrCookie);
            }
        } else {
            // redis不为空，cookie不为空，合并cookie和redis中购物车的商品数据（同一商品则覆盖redis）
            if (StringUtils.isNotBlank(shopCartStrCookie)) {
                /**
                 * 1. 已经存在的，把cookie中对应的数量，覆盖redis（参考京东）
                 * 2. 该项商品标记为待删除，统一放入一个待删除的list
                 * 3. 从cookie中清理所有的待删除list
                 * 4. 合并redis和cookie中的数据
                 * 5. 更新到redis和cookie中
                 */
                List<ShopCartBO> shopCartListRedis = JsonUtils.jsonToList(shopCartStrRedis, ShopCartBO.class);
                List<ShopCartBO> shopCartListCookie = JsonUtils.jsonToList(shopCartStrCookie, ShopCartBO.class);
                // 定义一个待删除 list
                List<ShopCartBO> pendingDeleteList = new ArrayList<>();
                for (ShopCartBO shopCarRedis : shopCartListRedis) {
                    String redisSpecId = shopCarRedis.getSpecId();
                    for (ShopCartBO shopCartCookie : shopCartListCookie) {
                        String cookieSpecId = shopCartCookie.getSpecId();
                        if (redisSpecId.equals(cookieSpecId)) {
                            // 覆盖购买数量，不累加，参考京东
                            shopCarRedis.setBuyCounts(shopCartCookie.getBuyCounts());
                            // 把 shopCartCookie 放入待删除列表，用于最后的删除与合并
                            pendingDeleteList.add(shopCartCookie);
                        }
                    }
                }
                // 从现有 cookie 中删除对应的覆盖过的商品数据
                shopCartListCookie.removeAll(pendingDeleteList);
                // 合并两个 list
                shopCartListRedis.addAll(shopCartListCookie);
                // 更新到 redis 和 cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopCartListRedis), true);
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopCartListRedis));
            } else {
                // redis 不为空，cookie 为空，直接把 redis 覆盖cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopCartStrRedis, true);
            }
        }
    }
}
