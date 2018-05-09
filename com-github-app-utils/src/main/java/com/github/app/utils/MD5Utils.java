package com.github.app.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class MD5Utils {
    private static final String HEX_NUMS_STR="0123456789ABCDEF";
    private static final int SALT_LENGTH = 12;

    /**
     *
     * @param hex
     * @return
     */
    private static byte[] hexStringToByte(String hex, int fromIndex, int length) {
        byte[] result = new byte[length/2];
        for (int i = 0; i < result.length; i++ ) {
            int pos = i * 2 + fromIndex;
            result[i] = (byte) (HEX_NUMS_STR.indexOf(hex.charAt(pos)) << 4
                    | HEX_NUMS_STR.indexOf(hex.charAt(pos + 1)));
        }
        return result;
    }

    /**
     * 将指定byte数组转换成16进制字符串
     * @param b
     * @return
     */
    private static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    /**
     *
     * @param msg
     * @return
     * @throws Exception
     */
    public static String md5(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(msg.getBytes());
            BigInteger lHashInt = new BigInteger(1, result);
            return String.format("%1$032X", lHashInt).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param msg
     * @return
     */
    public static String md5WithSalt(String msg) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt);
            md.update(msg.getBytes("UTF-8"));
            byte[] digest = md.digest();
            byte[] encrptBuf = new byte[digest.length + SALT_LENGTH];
            System.arraycopy(salt, 0, encrptBuf, 0, SALT_LENGTH);
            System.arraycopy(digest, 0, encrptBuf, SALT_LENGTH, digest.length);
            BigInteger lHashInt = new BigInteger(1, encrptBuf);
            return String.format("%1$032X", lHashInt).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param msg
     * @param encryptedMsg
     * @return
     */
    public static boolean validateMd5(String msg, String encryptedMsg) {
        return md5(msg).equalsIgnoreCase(encryptedMsg);
    }

    /**
     *
     * @param msg
     * @param encryptedMsg
     * @return
     */
    public static boolean validateMd5WithSalt(String msg, String encryptedMsg) {
        try {
            byte[] salt = hexStringToByte(encryptedMsg, 0, SALT_LENGTH * 2);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt);
            md.update(msg.getBytes("UTF-8"));
            byte[] digest = md.digest();

            byte[] other = hexStringToByte(encryptedMsg, 24, encryptedMsg.length() - SALT_LENGTH * 2);

            if(Arrays.equals(digest, other)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        String en = md5WithSalt("admin");
        System.out.println(en);
        System.out.println(validateMd5WithSalt("admin", en));
    }
}
