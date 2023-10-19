package com.xw.service.center;

import com.xw.pojo.Users;
import com.xw.pojo.bo.center.CenterUserBO;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/8/17
 */
public interface CenterUserService {

    /**
     * 检索用户 id 查询用户信息
     * @param userId
     * @return
     */
    public Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     * @param userId
     * @param centerUserBO
     * @return
     */
    public Users  updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 更新用户头像
     * @param userId
     * @param faceUrl
     * @return
     */
    public Users updateUserFace(String userId, String faceUrl);
}
