package com.udun_demo.support.utils;

public class SignUtil {

    public static String sign(String key,String timestamp,String nonce,String body) {
        return Md5.md5Digest(body + key + nonce + timestamp).toLowerCase();
    }
}