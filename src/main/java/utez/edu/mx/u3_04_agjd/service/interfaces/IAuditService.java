package utez.edu.mx.U3_04_AGJD.service.interfaces;

// PRINCIPIO SOLID: Interface Segregation Principle (ISP)
public interface IAuditService {
    void logAction(String action, String entity, String entityId, String details);
    void logAction(String action, String entity, String entityId, String details, String ipAddress, String userAgent);
}
