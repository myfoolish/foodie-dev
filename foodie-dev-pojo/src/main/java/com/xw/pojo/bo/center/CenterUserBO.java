 package com.xw.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

 /**
 * @author liuxiaowei
 * @Description
 * @date 2022/04/19
 */
@ApiModel(value ="用户对象BO" ,description = "从客户端，由用户传入的数据封装在此 entity 中")
public class CenterUserBO {

    @ApiModelProperty(value = "用户名", name = "username", example = "xw", required = true)
    private String username;
    @ApiModelProperty(value = "密码", name = "password", example = "123123", required = true)
    private String password;
    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123123", required = false)
    private String confirmPassword;
    @NotBlank(message = "昵称不能为空")
    @Length(max = 12, message = "昵称长度不能超过12位")
    @ApiModelProperty(value = "用户昵称", name = "nickName", example = "XwCoding", required = false)
    private String nickName;
     @Length(max = 12, message = "真实姓名长度不能超过12位")
    @ApiModelProperty(value = "真实姓名", name = "realname ", example = "XwCoding", required = false)
    private String realname;
     @Pattern(regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$",message = "请输入有效的手机号码")
    @ApiModelProperty(value = "手机号", name = "mobile", example = "17839199999", required = false)
    private String mobile;
     @Email
    @ApiModelProperty(value = "邮箱地址", name = "email", example = "xw@qq.com", required = false)
    private String email;
     @Min(value = 0, message = "性别选择不正确")
     @Max(value = 2, message = "性别选择不正确")
    @ApiModelProperty(value = "性别", name = "sex", example = "0:女 1:男 2:保密", required = false)
    private Integer sex;
    @ApiModelProperty(value = "生日", name = "birthday", example = "1900-01-01", required = false)
    private Date birthday;

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

     public String getNickName() {
         return nickName;
     }

     public void setNickName(String nickName) {
         this.nickName = nickName;
     }

     public String getRealname() {
         return realname;
     }

     public void setRealName(String realname) {
         this.realname = realname;
     }

     public String getMobile() {
         return mobile;
     }

     public void setMobile(String mobile) {
         this.mobile = mobile;
     }

     public String getEmail() {
         return email;
     }

     public void setEmail(String email) {
         this.email = email;
     }

     public Integer getSex() {
         return sex;
     }

     public void setSex(Integer sex) {
         this.sex = sex;
     }

     public Date getBirthday() {
         return birthday;
     }

     public void setBirthday(Date birthday) {
         this.birthday = birthday;
     }
 }
