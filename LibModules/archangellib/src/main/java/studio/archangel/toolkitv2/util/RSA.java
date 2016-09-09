/**
 *
 */
package studio.archangel.toolkitv2.util;

import android.util.Base64;

import java.io.FileInputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;

import javax.crypto.Cipher;

/**
 * @author Michael
 */
public class RSA {
    static String certi_path;

    /**
     * 为aes加密生成�?�?32位的key
     *
     * @return
     */
    public static String getKey() {
        String key = "";
        for (int i = 0; i < 32; i++) {
            key += (char) (Math.random() * (91 - 65) + 65);
        }
        return key;
    }


    /**
     * RSA加密
     *
     * @param data 待加密数�?
     * @return 加密后的数据
     */
    public static String rsaEncrypt(String data) {

        String result = null;
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            java.security.cert.Certificate cert = factory.generateCertificate(new FileInputStream(certi_path));
            PublicKey pk = cert.getPublicKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            //result = new BASE64Encoder().encode((cipher.doFinal(data.getBytes())));
            result = new String(Base64.encode(cipher.doFinal(data.getBytes()), Base64.DEFAULT), "utf-8");
//			for(byte bb:result.getBytes("utf-8")){
//				System.out.print(bb+",");
//			}
//			System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @return the certi_path
     */
    public static String getCerti_path() {
        return certi_path;
    }


    /**
     * @param certi_path the certi_path to set
     */
    public static void setCerti_path(String certi_path) {
        RSA.certi_path = certi_path;
    }

}