package com.xw.service.impl.center;

import com.xw.mapper.UsersMapper;
import com.xw.pojo.Users;
import com.xw.pojo.bo.center.CenterUserBO;
import com.xw.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/8/17
 */
@Service
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    private UsersMapper usersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Users result = usersMapper.selectByPrimaryKey(userId);
        result.setPassword(null);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED )
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users updateUser = new Users();
        BeanUtils.copyProperties(centerUserBO, updateUser);
        updateUser.setId(userId);
        updateUser.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(updateUser);
        return queryUserInfo(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED )
    @Override
    public Users updateUserFace(String userId, String faceUrl) {
        Users updateUser = new Users();
        updateUser.setId(userId);
        updateUser.setFace(faceUrl);
        updateUser.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(updateUser);
        return queryUserInfo(userId);
    }
}
