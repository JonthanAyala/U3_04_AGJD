package utez.edu.mx.U3_04_AGJD.service.interfaces;

// PRINCIPIO SOLID: Interface Segregation Principle (ISP)
public interface IEncryptionService {
    String encrypt(String data);
    String decrypt(String encryptedData);
    String hashData(String data);
    boolean verifyData(String data, String hashedData);
}
