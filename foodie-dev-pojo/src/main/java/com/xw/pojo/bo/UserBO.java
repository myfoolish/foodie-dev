package com.xw.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/8/20
 */
@ApiModel(value ="用户对象BO" ,description = "从客户端，由用户传入的数据封装在此 entity 中")
public class UserBO {

    @ApiModelProperty(value = "用户名", name = "username", example = "xw", required = true)
    private String username;
    @ApiModelProperty(value = "密码", name = "password", example = "123123", required = true)
    private String password;
    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123123", required = false)
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
