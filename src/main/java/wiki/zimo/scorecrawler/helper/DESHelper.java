package wiki.zimo.scorecrawler.helper;

import com.alibaba.fastjson.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

/**
 * 今日校园App Cpdaily-Extension字段加解密实现
 */
public class DESHelper {
    //    private static final String DEFAULT_KEY = "ST83=@XV";
    private static final String DEFAULT_KEY = "XCE927==";
    private static final byte[] IV = {1, 2, 3, 4, 5, 6, 7, 8};

    private static final String CHARSET_NAME = "utf-8";
    private static final String DES = "DES";
    private static final String CIPHER_NAME = "DES/CBC/PKCS5Padding";

    private static byte[] DESEncrypt(String data, String key, byte[] iv) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), DES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(data.getBytes(CHARSET_NAME));
    }

    private static String DESDecrypt(byte[] data, String key, byte[] iv) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), DES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return new String(cipher.doFinal(data), CHARSET_NAME);
    }

    /**
     * Base64编码
     *
     * @param data
     * @return
     */
    private static String Base64Encrypt(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Base64解码
     *
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    private static byte[] Base64Decrypt(String data) throws UnsupportedEncodingException {
        return Base64.getMimeDecoder().decode(data.getBytes(CHARSET_NAME));
    }

    public static String encryptDES(String data) throws NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
        return Base64Encrypt(DESEncrypt(data, DEFAULT_KEY, IV));
    }

    public static String decryptDES(String data) throws UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        return DESDecrypt(Base64Decrypt(data), DEFAULT_KEY, IV);
    }

    public static void main(String[] args) throws Exception {
        JSONObject object = new JSONObject();
        object.put("systemName", "android");
        object.put("systemVersion", "4.4.4");
        object.put("model", "OPPO R11 Plus");
        object.put("deviceId", UUID.randomUUID());
        object.put("appVersion", "8.1.11");
        object.put("lon", 0);
        object.put("lat", 0);
        object.put("userId", 161105024);
//        String s = encryptDES(object.toString());
//        System.out.println(encryptDES(object.toString()));
        String s = "f8nyr/FcxcZ44radTkUjFYSMwgh6ZH0ijtFTOlK2YAwmCd9OiAsuhRMziE0g CFH4HoNiywN5UH445XZUDnc0uAhvNCdKSTOnk075IOZoXafLmENE4NXp5+vu iWxvpIockDfICF9DEOok9o/uq4aWdGRy3bxSZqUqyBFHToVgPtOs9tx9T6iS XDLxtUDXaZOskxKHAUrR3gaIP+y8rbsC6+mEVyzWGuLb";
        System.out.println(decryptDES(s));
    }
}
