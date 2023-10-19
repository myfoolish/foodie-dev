package com.xw.controller.center;

import com.xw.pojo.Users;
import com.xw.service.center.CenterUserService;
import com.xw.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/4/18
 */
@Api(value = "用户中心",tags = {"用户中心相关接口"})
@RestController
@RequestMapping("/center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息 ", httpMethod = "GET")
    @RequestMapping("/userInfo")
    public JSONResult userInfo(@RequestParam  String userId) {
        Users users = centerUserService.queryUserInfo(userId);
        return JSONResult.ok(users);

    }
}
