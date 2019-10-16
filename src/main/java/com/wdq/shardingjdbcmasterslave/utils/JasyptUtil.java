package com.wdq.shardingjdbcmasterslave.utils;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @description: 加密数据库明文密码
 * @author: wdq-bg405275
 * @data: 2019-10-15 10:54
 **/
public class JasyptUtil {
    /**
     * 加密方法
     * @param salt 盐值
     * @param targetString 待加密字符串
     * @return 密文
     */
    public static String encrypt(String salt, String targetString) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(salt);
        return encryptor.encrypt(targetString);
    }

    /**
     * 解密方法
     * @param salt 盐值
     * @param targetString 待解密字符串
     * @return 明文
     */
    public static String decrypt(String salt,String targetString) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(salt);
        return encryptor.decrypt(targetString);
    }

    public static void main(String[] args) {
        String salt = "shardingjdbc-demo";
        String password = "123456";
        // 进行加密操作
        String encryptString1 = encrypt(salt, password);
        // 进行解密操作
        String decryptString1 = decrypt(salt, encryptString1);
        // 输出明文和密文
        System.out.println("encryptString1="+encryptString1);
        System.out.println("decryptString1="+decryptString1);
    }
}
