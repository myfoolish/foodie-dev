package com.xw.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

/**
 * @author liuxiaowei
 * @Description 对字符串进行 md5 加密
 * @date 2021/8/20
 */
public class MD5Utils {

    /**
     *
     * @param strValue
     * @return
     * @throws Exception
     */
    public static String getMD5Str(String strValue) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String newStr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
        return newStr;
    }

    public static void main(String[] args) {
        try {
            String md5 = getMD5Str("xw");
            System.out.println(md5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
