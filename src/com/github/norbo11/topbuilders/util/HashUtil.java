package com.github.norbo11.topbuilders.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
    
    public static String generateMD5Hash(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            md.update(string.getBytes());
            return convertByteArrayToHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            Log.info(e);
        }
        return null;
    }
}
