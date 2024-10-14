package myapp.service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptDecript {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";

    // Define a fixed key (must be 16, 24, or 32 bytes for AES)
    private static final String key = "MinhaChaveSuperS"; // Use a strong key in production
    // Define a fixed IV (must be 16 bytes for AES)
    private static final byte[] FIXED_IV = new byte[16]; // Initialize to zero or set a specific value

    public static String encrypt(String data) throws Exception {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(FIXED_IV);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedData) throws Exception {
        byte[] encrypted = Base64.getDecoder().decode(encryptedData);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(FIXED_IV);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] original = cipher.doFinal(encrypted);
        return new String(original);
    }

    public static void main(String[] args) throws Exception {
        String data = "Minha mensagem secreta";

        String encrypted = encrypt(data);
        String decrypted = decrypt(encrypted);

        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
}
