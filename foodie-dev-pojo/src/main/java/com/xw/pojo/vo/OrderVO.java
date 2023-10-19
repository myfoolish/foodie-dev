package com.xw.pojo.vo;

import com.xw.pojo.bo.ShopCartBO;

import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/4/8
 */
public class OrderVO {

    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;
    private List<ShopCartBO> toBeRemovedShopCartList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrdersVO getMerchantOrdersVO() {
        return merchantOrdersVO;
    }

    public void setMerchantOrdersVO(MerchantOrdersVO merchantOrdersVO) {
        this.merchantOrdersVO = merchantOrdersVO;
    }

    public List<ShopCartBO> getToBeRemovedShopCartList() {
        return toBeRemovedShopCartList;
    }

    public void setToBeRemovedShopCartList(List<ShopCartBO> toBeRemovedShopCartList) {
        this.toBeRemovedShopCartList = toBeRemovedShopCartList;
    }
}