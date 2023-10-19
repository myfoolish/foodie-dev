package com.xw.enums;

/**
 * @author liuxiaowei
 * @Description 分类 枚举
 * @date 2021/12/31
 */
public enum CategoryLevel {
    ROOT(1,"一级分类"),
    SUB(2,"二级分类"),
    CHILD(3,"三级分类");

    public final Integer type;
    public final String value;

    CategoryLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
