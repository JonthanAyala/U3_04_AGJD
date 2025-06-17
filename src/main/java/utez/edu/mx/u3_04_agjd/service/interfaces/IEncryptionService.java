package utez.edu.mx.u3_04_agjd.service.interfaces;


public interface IEncryptionService {
    String encrypt(String data);
    String decrypt(String encryptedData);
    String hashData(String data);
    boolean verifyData(String data, String hashedData);
}
