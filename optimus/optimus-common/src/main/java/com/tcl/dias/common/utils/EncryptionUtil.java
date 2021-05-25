package com.tcl.dias.common.utils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * Created by raja
 */
public class EncryptionUtil {

    private static final String ALGORITHM = "PBEWithMD5AndDES";
    private static final String CHARSET = "UTF-8";
    private static final char[] PASSWORD = "wubbalubbadubdub".toCharArray(); //// TODO: 11-01-2017 shouldn't be hardcoded
    private static final int ITERATION_COUNT = 20;
    private static final byte[] SALT = {(byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12, (byte) 0xde, (byte) 0x33,
            (byte) 0x10, (byte) 0x12,};

    public static String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance(ALGORITHM);
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, ITERATION_COUNT));
        return base64Encode(pbeCipher.doFinal(property.getBytes(CHARSET)));
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(String property) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance(ALGORITHM);
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, ITERATION_COUNT));
        return new String(pbeCipher.doFinal(base64Decode(property)), CHARSET);
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }

    public static String getHmacShaHash(String textToHash, String saltKey) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec keySpec = new SecretKeySpec(saltKey.getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
        byte[] hashBytes = mac.doFinal(textToHash.getBytes());
        return Hex.encodeHexString(hashBytes);
    }
}
