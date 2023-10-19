package com.xw.service;

import com.xw.pojo.Users;
import com.xw.pojo.bo.UserBO;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/8/17
 */
public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     * @param userBO
     * @return
     */
    public Users createUser(UserBO userBO);

    /**
     * 检索用户名和密码是否匹配，用于登录
     * @param username
     * @param password
     * @return
     */
    public Users queryUserForLogin(String username, String password);
}
