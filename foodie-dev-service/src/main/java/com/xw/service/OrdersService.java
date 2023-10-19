package com.xw.service;

import com.xw.pojo.OrderStatus;
import com.xw.pojo.bo.ShopCartBO;
import com.xw.pojo.bo.SubmitOrderBO;
import com.xw.pojo.vo.OrderVO;

import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/12/31
 */
public interface OrdersService {

    /**
     * 用于创建订单相关信息
     * @param submitOrderBO
     * @return
     */
    public OrderVO createOrders(List<ShopCartBO> shopCartList,  SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    public OrderStatus queryOrderStatuesInfo(String orderId);

    /**
     * 关闭超时未支付订单
     */
    public void closeOrder();
}
