package com.xw.controller;

import com.xw.pojo.UserAddress;
import com.xw.pojo.bo.AddressBO;
import com.xw.service.AddressService;
import com.xw.utils.JSONResult;
import com.xw.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/1/17
 */
@Api(value = "收货地址",tags = {"用于购物车地址的相关接口"})
@RestController
@RequestMapping("/address")
public class AddressController {

    /**
     * 用户在确认订单页面，可以针对收货地址作如下操作：
     * 1、查询用户的所有收货地址列表
     * 2、新增收货地址
     * 3、删除收货地址
     * 4、修改收货地址
     * 5、设置默认收货地址
     */

    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
    @PostMapping("/list")
    public JSONResult list(@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("");
        }
        List<UserAddress> result = addressService.queryAll(userId);
        return JSONResult.ok(result);
    }

    @ApiOperation(value = "新增用户收货地址", notes = "新增用户收货地址", httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(@RequestBody AddressBO addressBO) {

        JSONResult checkAddress = checkAddressInfo(addressBO);
        if (checkAddress.getStatus() != 200) {
            return checkAddress;
        }
        addressService.addNewUserAddress(addressBO);
        return JSONResult.ok();
    }

    @ApiOperation(value = "修改用户收货地址", notes = "修改用户收货地址", httpMethod = "POST")
    @PostMapping("/update")
    public JSONResult update(@RequestBody AddressBO addressBO) {
        String addressId = addressBO.getAddressId();
        if (StringUtils.isBlank(addressId)) {
            return JSONResult.errorMsg("修改地址错误：addressId不能为空");
        }
        JSONResult checkAddress = checkAddressInfo(addressBO);
        if (checkAddress.getStatus() != 200) {
            return checkAddress;
        }
        addressService.updateUserAddress(addressBO);
        return JSONResult.ok();
    }

    private JSONResult checkAddressInfo(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return JSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return JSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return JSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return JSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return JSONResult.errorMsg("收货人手机号格式不正确");
        }
        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province)
                || StringUtils.isBlank(city)
                || StringUtils.isBlank(district)
                || StringUtils.isBlank(detail)) {
            return JSONResult.errorMsg("收货人地址信息不能为空");
        }
        return JSONResult.ok();
    }

    @ApiOperation(value = "删除用户收货地址", notes = "删除用户收货地址", httpMethod = "POST")
    @PostMapping("/delete")
    public JSONResult delete(@RequestParam String userId, @RequestParam String addressId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return JSONResult.errorMsg("");
        }
        addressService.deleteUserAddress(userId, addressId);
        return JSONResult.ok();
    }

    @ApiOperation(value = "设置用户默认收货地址", notes = "设置用户默认收货地址", httpMethod = "POST")
    @PostMapping("/setDefault")
    public JSONResult setDefault(@RequestParam String userId, @RequestParam String addressId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return JSONResult.errorMsg("");
        }
        addressService.updateUserAddressToBeDefault(userId, addressId);
        return JSONResult.ok();
    }
}
