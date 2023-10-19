package com.xw.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.xw.resource.FileResource;
import com.xw.service.FastDFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/12/7
 */
@Service
public class FastDFSServiceImpl implements FastDFSService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private FileResource fileResource;

    @Override
    public String upload(MultipartFile file, String fileExtName) throws Exception {
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileExtName, null);
        String path = storePath.getFullPath();
        return path;
    }

    @Override
    public String uploadOSS(MultipartFile file, String userId, String fileExtName) throws Exception {
        // 构建ossClient
        OSS ossClient = new OSSClientBuilder().build(
                fileResource.getEndpoint(),
                fileResource.getAccessKeyId(),
                fileResource.getAccessKeySecret());

        InputStream inputStream = file.getInputStream();
        String myObjectName = fileResource.getObjectName() + "/" + userId + "/" + userId + "." + fileExtName;
        ossClient.putObject(fileResource.getBucketName(), myObjectName, inputStream);
        ossClient.shutdown();
        return myObjectName;
    }
}
