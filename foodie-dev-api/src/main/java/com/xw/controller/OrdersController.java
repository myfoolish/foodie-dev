package com.xw.controller;

import com.xw.enums.OrderStatusEnum;
import com.xw.enums.PayMethod;
import com.xw.pojo.bo.ShopCartBO;
import com.xw.pojo.bo.SubmitOrderBO;
import com.xw.pojo.vo.MerchantOrdersVO;
import com.xw.pojo.vo.OrderVO;
import com.xw.service.OrdersService;
import com.xw.utils.CookieUtils;
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
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/7/21
 */
@Api(value = "订单",tags = {"用于订单的相关接口"})
@RestController
@RequestMapping("/orders")
public class OrdersController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "创建订单", notes = "创建订单", httpMethod = "POST")
    @RequestMapping("/create")
    public JSONResult create(
            @ApiParam(name = "submitOrderBO", value = "订单对象", required = true) @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return JSONResult.errorMsg("支付方式不支持");
        }
        logger.info("购物车数据为：" + submitOrderBO);
        String shopCartStr = redisOperator.get(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId());
        if (StringUtils.isBlank(shopCartStr)) {
            return JSONResult.errorMsg("购物车数据不正确");
        }
        List<ShopCartBO> shopCartList = JsonUtils.jsonToList(shopCartStr, ShopCartBO.class);

        // 1、创建订单
        OrderVO orderVO = ordersService.createOrders(shopCartList, submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2、移除购物车中已结算（已提交）的商品
        // 清理覆盖现有的 redis 汇总的购物车数据
        shopCartList.removeAll(orderVO.getToBeRemovedShopCartList());
        redisOperator.set(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId(), JsonUtils.objectToJson(shopCartList));
        // 整合 redis 之后，完善购物车中的已结算商品清楚，并同步到前端的 cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopCartList), true);

        // 3、 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        // 为了方便测试支付，所有的支付金额统一改为1分钱

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId","xw");
        headers.add("password","123123");

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<JSONResult> responseEntity = restTemplate.postForEntity(paymentUrl, entity, JSONResult.class);
        JSONResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            return JSONResult.errorMsg("支付中心创建订单失败，请联系管理员！");
        }

        return JSONResult.ok(orderId);
    }

    @ApiOperation(value = "通知后台修改订单状态", notes = "通知后台修改订单状态", httpMethod = "POST")
    @RequestMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(
            @ApiParam(name = "merchantOrderId", value = "商户订单 id", required = true) String merchantOrderId) {
        ordersService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }
}
