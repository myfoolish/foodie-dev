package com.xw.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/12/7
 */
public interface FastDFSService {

    public String upload(MultipartFile file, String fileExtName) throws Exception;

    /**
     * ALiYun OSS
     * @param file
     * @param userId
     * @param fileExtName
     * @return
     * @throws Exception
     */
    public String uploadOSS(MultipartFile file, String userId, String fileExtName) throws Exception;
}
