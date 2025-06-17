package utez.edu.mx.u3_04_agjd.service;

import utez.edu.mx.u3_04_agjd.model.AuditLog;
import utez.edu.mx.u3_04_agjd.repository.AuditLogRepository;
import utez.edu.mx.u3_04_agjd.service.interfaces.IAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

// PRINCIPIO SOLID: Single Responsibility Principle (SRP) - Solo maneja auditoría
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService implements IAuditService {
    
    private final AuditLogRepository auditLogRepository;
    
    @Override
    public void logAction(String action, String entity, String entityId, String details) {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String ipAddress = null;
            String userAgent = null;
            
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                ipAddress = getClientIpAddress(request);
                userAgent = request.getHeader("User-Agent");
            }
            
            logAction(action, entity, entityId, details, ipAddress, userAgent);
            
        } catch (Exception e) {
            log.error("Error guardando log de auditoría", e);
        }
    }
    
    @Override
    public void logAction(String action, String entity, String entityId, String details, String ipAddress, String userAgent) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setAccion(action);
            auditLog.setEntidad(entity);
            auditLog.setEntidadId(entityId);
            auditLog.setDetalles(details);
            auditLog.setIpAddress(ipAddress);
            auditLog.setUserAgent(userAgent);
            
            auditLogRepository.save(auditLog);
            
            log.info("Audit Log: Acción={}, Entidad={}, ID={}, IP={}", 
                    action, entity, entityId, ipAddress);
            
        } catch (Exception e) {
            log.error("Error guardando log de auditoría", e);
        }
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
            "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", 
            "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", 
            "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", 
            "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", 
            "HTTP_VIA", "REMOTE_ADDR"
        };
        
        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0];
            }
        }
        
        return request.getRemoteAddr();
    }
}
