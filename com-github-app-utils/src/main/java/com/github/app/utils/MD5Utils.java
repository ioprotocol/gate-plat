package com.github.app.utils;

import org.apache.commons.codec.digest.Md5Crypt;

public class MD5Utils {
    private static final String MD5_PREFIX = "#$#";


    /**
     * @param msg
     * @return
     */
    public static String md5WithSalt(String msg) {
        try {
            return Md5Crypt.md5Crypt(msg.getBytes("utf-8"), null, MD5_PREFIX);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param msg
     * @param encryptedMsg
     * @return
     */
    public static boolean validateMd5WithSalt(String msg, String encryptedMsg) {
        try {
            String salt = encryptedMsg.substring(0, encryptedMsg.lastIndexOf("$"));
            String pwd = Md5Crypt.md5Crypt(msg.getBytes("utf-8"), salt, MD5_PREFIX);
            return pwd.equals(encryptedMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        String en = md5WithSalt("123456");
        System.out.println(en);
        System.out.println(validateMd5WithSalt("123456", en));
    }
}
