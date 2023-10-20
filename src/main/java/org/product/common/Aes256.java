package org.product.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.product.exception.ApiException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
public class Aes256 {

    final private static String IV = "a&9fql3@jDAE2f8#";
    final private static String KEY = "*D87@a#343!D23dc";

    // 암호화
    public static String encrypt(String str) {
        return encrypt(str, IV);
    }

    public static String encrypt(String str, String ivString) {
        try {
            byte[] keyBytes = KEY.substring(0, 16).getBytes("UTF-8");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            String iv = getIv(ivString);

            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));

            byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
            String enStr = new String(Base64.encodeBase64(encrypted));

            return enStr;
        } catch(Exception e) {
            throw new ApiException("암호화 요청 데이터를 확인해 주시기 바랍니다.");
        }
    }

    //복호화
    public static String decrypt(String str) {
        return decrypt(str, IV);
    }

    public static String decrypt(String str, String ivString) {
        try {
            byte[] keyBytes = KEY.substring(0, 16).getBytes("UTF-8");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            String iv = getIv(ivString);
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));

            byte[] byteStr = Base64.decodeBase64(str.getBytes());

            return new String(c.doFinal(byteStr),"UTF-8");
        } catch(Exception e) {
            throw new ApiException("비정상 요청 데이터 입니다. 요청 데이터를 확인해 주시기 바랍니다.");
        }
    }

    public static String getIv(String iv) {
        if (iv == null) {
            return IV.substring(0, 16);
        } else if (iv.length() >= 15) {
            return iv.substring(0, 16);
        } else {
            return StringUtils.rightPad(iv, 32).substring(0, 16);
        }
    }
}
