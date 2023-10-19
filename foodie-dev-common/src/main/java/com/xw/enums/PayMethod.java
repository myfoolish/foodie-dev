package com.xw.enums;

/**
 * @author liuxiaowei
 * @Description 支付方式 枚举
 * @date 2021/12/31
 */
public enum PayMethod {
    WEIXIN(1,"微信"),
    ALIPAY(2,"支付宝");

    public final Integer type;
    public final String value;

    PayMethod(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
