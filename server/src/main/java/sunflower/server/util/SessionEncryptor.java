package sunflower.server.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class SessionEncryptor {

    private final String secretKey;

    public SessionEncryptor(
            @Value("${encryption.secret-key}") String secretKey
    ) {
        this.secretKey = secretKey;
    }

    public String encrypt(Long sessionId) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(sessionId.toString().getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long decrypt(String encryptedSessionId) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedSessionId));
            return Long.parseLong(new String(decrypted));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
