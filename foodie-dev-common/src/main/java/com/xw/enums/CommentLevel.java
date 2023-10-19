package com.xw.enums;

/**
 * @author liuxiaowei
 * @Description 商品评价等级 枚举
 * @date 2021/12/31
 */
public enum CommentLevel {
    GOOD(1,"好评"),
    NORMAL(2,"中评"),
    BAD(3,"差评");

    public final Integer type;
    public final String value;

    CommentLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
