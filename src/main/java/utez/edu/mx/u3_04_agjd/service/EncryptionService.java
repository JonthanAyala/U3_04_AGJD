package utez.edu.mx.U3_04_AGJD.service;

import utez.edu.mx.U3_04_AGJD.service.interfaces.IEncryptionService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.util.Base64;

// PRINCIPIO SOLID: Single Responsibility Principle (SRP) - Solo maneja cifrado
@Service
public class EncryptionService implements IEncryptionService {
    
    private final SecretKey secretKey;
    private final String algorithm = "AES";
    
    public EncryptionService() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(256);
            this.secretKey = keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Error inicializando servicio de cifrado", e);
        }
    }
    
    @Override
    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Error cifrando datos", e);
        }
    }
    
    @Override
    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Error descifrando datos", e);
        }
    }
    
    @Override
    public String hashData(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error generando hash", e);
        }
    }
    
    @Override
    public boolean verifyData(String data, String hashedData) {
        try {
            String newHash = hashData(data);
            return newHash.equals(hashedData);
        } catch (Exception e) {
            return false;
        }
    }
}
