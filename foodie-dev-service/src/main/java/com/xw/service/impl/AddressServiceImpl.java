package com.xw.service.impl;

import com.xw.enums.YesOrNo;
import com.xw.mapper.UserAddressMapper;
import com.xw.pojo.UserAddress;
import com.xw.pojo.bo.AddressBO;
import com.xw.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/12/31
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        // 1、判断当前用户是否存在地址，如果没有，则新增为：默认地址
        Integer isDefault = 0;
        List<UserAddress> userAddressList = this.queryAll(addressBO.getAddressId());
        if (userAddressList == null || userAddressList.isEmpty() || userAddressList.size() == 0) {
            isDefault = 1;
        }

        String addressId = sid.nextShort();

        // 2、保存地址到数据库
        UserAddress newUserAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, newUserAddress);
        newUserAddress.setId(addressId);
        newUserAddress.setIsDefault(isDefault);
        newUserAddress.setCreatedTime(new Date());
        newUserAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(newUserAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {
        String addressId = addressBO.getAddressId();
//        UserAddress pendingAddress = new UserAddress();
        UserAddress updateUserAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, updateUserAddress);
        updateUserAddress.setId(addressId);
        updateUserAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(updateUserAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress address = new UserAddress();
        address.setId(addressId);
        address.setUserId(userId);
        userAddressMapper.delete(address);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) {
        // 1、查找默认地址设置为不默认
        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> list = userAddressMapper.select(queryAddress);
        for (UserAddress address : list) {
            address.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(address);
        }
        // 2、根据地址id修改为默认的地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setUserId(userId);
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress singleAddress = new UserAddress();
        singleAddress.setId(addressId);
        singleAddress.setUserId(userId);
        return userAddressMapper.selectOne(singleAddress);
    }
}
