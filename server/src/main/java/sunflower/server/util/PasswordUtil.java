package sunflower.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class PasswordUtil {

    private static final String ALGORITHM = "SHA-256";

    public static String encrypt(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(password.getBytes());
            return Base64.getEncoder().encodeToString(digest.digest());
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalArgumentException("암호화 알고리즘에 오류가 발생했습니다.");
        }
    }
}
