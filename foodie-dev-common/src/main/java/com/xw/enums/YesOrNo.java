package com.xw.enums;

/**
 * @author liuxiaowei
 * @Description 是否 枚举
 * @date 2021/12/31
 */
public enum YesOrNo {
    NO(0,"否"),
    YES(1,"是");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
