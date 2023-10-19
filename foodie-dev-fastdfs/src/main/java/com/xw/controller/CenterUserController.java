package com.xw.controller;

import com.xw.pojo.Users;
import com.xw.pojo.vo.UsersVO;
import com.xw.resource.FileResource;
import com.xw.service.FastDFSService;
import com.xw.service.center.CenterUserService;
import com.xw.utils.CookieUtils;
import com.xw.utils.JSONResult;
import com.xw.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/4/18
 */
@RestController
@RequestMapping("/fastDFS")
public class CenterUserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CenterUserController.class);
    @Autowired
    private FastDFSService fastDFSService;

    @Autowired
    private FileResource fileResource;

    @Autowired
    private CenterUserService centerUserService;

    @RequestMapping("/uploadFace")
    public JSONResult updateFace(String userId, MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 开始文件上传
        String path = "";
        if (file != null) {
            FileOutputStream fileOutputStream = null;
            // 获取文件上传的文件名称
            String fileName = file.getOriginalFilename();
            if (StringUtils.isNotBlank(fileName)) {
                // 文件重命名 xw.png -- >["xw", "png"]
                String[] fileNameArr = fileName.split("\\.");
                // 获取文件的后缀名
                String suffix = fileNameArr[fileNameArr.length - 1];

                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg")) {
                    return JSONResult.errorMsg("图片格式不正确!");
                }
                path = fastDFSService.upload(file, suffix);
                // ALiYun OSS
//                path = fastDFSService.uploadOSS(file, userId, suffix);
                logger.info("图片上传路径为：{}", path);
            }
        } else {
            return JSONResult.errorMsg("文件不能为空！");
        }
        if (StringUtils.isNotBlank(path)) {
            String finalUserFaceUrl = fileResource.getHost() + path;
            // ALiYun OSS
//            String finalUserFaceUrl = fileResource.getOssHost() + path;
            // 更新用户头像到数据库
            Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);
//        userResult = setNullProperty(userResult);

            // 增加令牌token，会整合进redis，分布式会话
            UsersVO usersVO = conventUsersVO(userResult);

            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);
        } else {
           return JSONResult.errorMsg("上传头像失败！");
        }
        return JSONResult.ok();
    }
}
