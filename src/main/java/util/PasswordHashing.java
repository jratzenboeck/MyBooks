package util;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class PasswordHashing {

    private static final int ITERATIONS = 10;
    private static final int SALT_LENGTH = 32;
    private static final int KEY_LENGTH = 256;

    public static byte[] hashPassword(final char[] passwordPlain, final byte[] salt) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec(passwordPlain, salt, ITERATIONS, KEY_LENGTH);
            SecretKey key = skf.generateSecret( spec );
            return key.getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generateSalt() {
        try {
            byte[] salt = new byte[SALT_LENGTH];
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
