/**
 *
 */
package studio.archangel.toolkitv2.util;

/**
 * @author Michael
 */

import android.util.Base64;

import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    public static String decrypt(String data, String key) {
        Cipher cipher;
        SecretKeySpec keySpec;
        AlgorithmParameterSpec iv;
        String result = "";
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            iv = getIV();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(key.getBytes("UTF-8"));
            byte[] keyBytes = new byte[32];
            System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
            keySpec = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            // byte[] bytes = new BASE64Decoder().decodeBuffer(data);
            byte[] bytes = Base64.decode(data, Base64.DEFAULT);
            bytes = cipher.doFinal(bytes);
            result = new String(bytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static AlgorithmParameterSpec getIV() {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);
        return ivParameterSpec;
    }
}