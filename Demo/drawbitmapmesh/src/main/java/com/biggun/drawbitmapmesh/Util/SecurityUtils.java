package com.biggun.drawbitmapmesh.Util;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 作者：孙贤武 on 2016/3/18 14:24
 * 邮箱：sun91985415@163.com
 */
public class SecurityUtils
{
    private static class Load
    {
        private static  SecurityUtils securityUtils = new SecurityUtils();
    }
    private SecurityUtils()
    {

    }
    public static SecurityUtils getInstance()
    {
        return Load.securityUtils;
    }

    private static final String RSA = "RSA";
    private static final String DES = "DES";

    /**
     * 随机生成RSA密钥对
     *
     * @param length 密钥长度，范围：512～2048<br>
     *               一般1024
     * @return
     */
    public KeyPair getKeyPair(int length)
    {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(length);
            return kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用公钥对数据加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public byte[] enCrypt(byte[] data, PublicKey publicKey)
    {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 用私钥对数据解密
     *
     * @param data
     * @param privateKey
     * @return
     */
    public byte[] deCrypt(byte[] data, PrivateKey privateKey)
    {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从字符串中获取到公钥
     *
     * @param keys
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PublicKey loadPublicKey(String keys)
    {
        byte[] bytes = Base64.decode(keys.getBytes(), Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("没有此算法:" + e.getLocalizedMessage());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            System.out.println("公钥非法:" + e.getLocalizedMessage());
        }
        return null;
    }

    public PublicKey loadPublicKey(InputStream is)
    {
        try {
            return loadPublicKey(readStream(is));
        } catch (IOException e) {
            System.out.println("公钥输入流读取失败:" + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }

    public PrivateKey loadPrivateKey(String keys)
    {
        byte[] bytes = Base64.decode(keys.getBytes(), Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("没有此算法:" + e.getLocalizedMessage());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            System.out.println("私钥非法:" + e.getLocalizedMessage());
        }
        return null;
    }

    public PrivateKey loadPrivateKey(InputStream is)
    {
        try {
            return loadPrivateKey(readStream(is));
        } catch (IOException e) {
            System.out.println("私钥输入流读取失败:" + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String readStream(InputStream is) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.charAt(0) == '-') {
                continue;
            } else {
                stringBuilder.append(line);
                stringBuilder.append("\r");
            }
        }
        return stringBuilder.toString();
    }


    /**
     * 打印公钥信息
     *
     * @param publicKey
     */
    public void printPublicKeyInfo(PublicKey publicKey)
    {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
        System.out.println("----------RSAPublicKey----------");
        System.out.println("Modulus.length=" + rsaPublicKey.getModulus().bitLength());
        System.out.println("Modulus=" + rsaPublicKey.getModulus().toString());
        System.out.println("PublicExponent.length=" + rsaPublicKey.getPublicExponent().bitLength());
        System.out.println("PublicExponent=" + rsaPublicKey.getPublicExponent().toString());
    }

    /**
     * 打印私钥信息
     *
     * @param privateKey
     */
    public void printPrivateKeyInfo(PrivateKey privateKey)
    {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
        System.out.println("----------RSAPrivateKey ----------");
        System.out.println("Modulus.length=" + rsaPrivateKey.getModulus().bitLength());
        System.out.println("Modulus=" + rsaPrivateKey.getModulus().toString());
        System.out.println("PrivateExponent.length=" + rsaPrivateKey.getPrivateExponent().bitLength());
        System.out.println("PrivatecExponent=" + rsaPrivateKey.getPrivateExponent().toString());
    }

    /**
     * des加密
     * @param data
     * @param password 密钥最少8位
     * @return
     */
    public byte[] desenCrypt(byte[] data,String password)
    {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec keySpec = new DESKeySpec(password.getBytes());
            SecretKeyFactory instance = SecretKeyFactory.getInstance(DES);
            SecretKey secretKey = instance.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(cipher.ENCRYPT_MODE,secretKey,random);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * des解密
     * @param data
     * @param password 密钥最少8位  desede则是24位
     * @return
     */
    public byte[] desdeCrypt(byte[] data,String password)
    {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec keySpec = new DESKeySpec(password.getBytes());
            SecretKeyFactory instance = SecretKeyFactory.getInstance(DES);
            SecretKey secretKey = instance.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.DECRYPT_MODE,secretKey,random);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
