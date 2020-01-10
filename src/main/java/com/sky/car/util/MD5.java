package com.sky.car.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
 
    public static String md5(String inputStr) {
        String pwd = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] s = digest.digest(inputStr.getBytes("UTF-8"));
            pwd = toHex(s);
            return pwd;
        } catch (NoSuchAlgorithmException var4) {
            System.err.println("Failed to load the MD5 MessageDigest. Jive will be unable to function normally.");
            var4.printStackTrace();
            return null;
        } catch (Exception var5) {
            System.err.println("Failed to load the MD5 MessageDigest. Jive will be unable to function normally.");
            var5.printStackTrace();
            return null;
        }
    }
    
    public static final String toHex(byte[] hash) {
        StringBuffer buf = new StringBuffer(hash.length * 2);

        for(int i = 0; i < hash.length; ++i) {
            if ((hash[i] & 255) < 16) {
                buf.append("0");
            }

            buf.append(Long.toString((long)(hash[i] & 255), 16));
        }

        return buf.toString();
    }
	
}
