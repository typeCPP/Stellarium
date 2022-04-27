package com.app.stellarium.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordEncoder {
    public static String encodePasswordMD5(String password) {
        try {
            String salt = "jiFNCqikRSnqstPS7wso";
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] message = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : message) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
