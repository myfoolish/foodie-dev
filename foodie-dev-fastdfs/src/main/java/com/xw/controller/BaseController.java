package com.xw.controller;

import com.xw.pojo.Orders;
import com.xw.pojo.Users;
import com.xw.pojo.vo.UsersVO;
import com.xw.service.center.MyOrdersService;
import com.xw.utils.JSONResult;
import com.xw.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.util.UUID;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/7/21
 */
@ApiIgnore  // 忽略此 控制类
@RestController
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    // 购物车
    public static final String FOODIE_SHOPCART = "shopcart";
    // 评论分页条数
    public static final Integer COMMON_PAGE_SIZE = 10;
    // 通用分页条数
    public static final Integer PAGE_SIZE = 20;

    @Autowired
    private RedisOperator redisOperator;

    public static final String REDIS_USER_TOKEN = "redis_user_token";

    // 支付中心的调用地址
//    String paymentUrl = "https://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
    String paymentUrl = "http://localhost:8089/payment/createMerchantOrder";

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                       -> 回调通知的 url
    public static String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";

    // 用户上传头像的位置
    //    public static final String IMAGE_USER_FACE_LOCATION = "/Users/liuxiaowei/downloads/foodie/faces";
    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "Users" +
                                                            File.separator + "liuxiaowei" +
                                                            File.separator + "downloads" +
                                                            File.separator + "foodie" +
                                                            File.separator + "faces";

    @Autowired
    public MyOrdersService myOrdersService;

    /**
     * 用于验证用户和订单是否有关联关系，避免非法用户调用
     * @return
     */
    public JSONResult checkUserOrder(String userId, String orderId) {
        Orders order = myOrdersService.queryMyOrders(userId, orderId);
        if (order == null) {
            return JSONResult.errorMsg("订单不存在！");
        }
        return JSONResult.ok(order);
    }

    public UsersVO conventUsersVO(Users users) {
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN + ":" + users.getId(), uniqueToken);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users, usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        return usersVO;
    }
}
