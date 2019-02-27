package com.unidt.helper.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Helper {

    /**
     * 计算给定字符串的MD5码
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String md5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] srcBytes = str.getBytes();
        byte[] md5bytes = md5.digest(srcBytes);
        StringBuffer buffer = new StringBuffer();
        for ( int i = 0 ; i < md5bytes.length; i++) {
            buffer.append(Integer.toHexString((md5bytes[i] >>> 4 & 0x0f)));
            buffer.append(Integer.toHexString((md5bytes[i] & 0x0f)));
        }
        return buffer.toString().toLowerCase();
    }
}
