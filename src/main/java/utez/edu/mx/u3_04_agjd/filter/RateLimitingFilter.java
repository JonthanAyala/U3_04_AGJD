package utez.edu.mx.U3_04_AGJD.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

// PRINCIPIO DE SEGURIDAD: Rate Limiting para prevenir ataques DoS (implementación simple sin bucket4j)
@Slf4j
@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    
    private final ConcurrentHashMap<String, RequestInfo> requestCounts = new ConcurrentHashMap<>();
    private final int MAX_REQUESTS_PER_MINUTE = 100;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String clientIp = getClientIpAddress(request);
        
        if (isRateLimited(clientIp)) {
            log.warn("Rate limit exceeded for IP: {}", clientIp);
            response.setStatus(429);
            response.getWriter().write("{\"error\":\"Rate limit exceeded. Try again later.\"}");
            response.setContentType("application/json");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean isRateLimited(String clientIp) {
        LocalDateTime now = LocalDateTime.now();
        RequestInfo requestInfo = requestCounts.computeIfAbsent(clientIp, k -> new RequestInfo());
        
        // Limpiar contadores antiguos (más de 1 minuto)
        if (requestInfo.getLastRequest().isBefore(now.minusMinutes(1))) {
            requestInfo.setCount(0);
        }
        
        requestInfo.setLastRequest(now);
        requestInfo.setCount(requestInfo.getCount() + 1);
        
        return requestInfo.getCount() > MAX_REQUESTS_PER_MINUTE;
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }
    
    private static class RequestInfo {
        private int count = 0;
        private LocalDateTime lastRequest = LocalDateTime.now();
        
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        public LocalDateTime getLastRequest() { return lastRequest; }
        public void setLastRequest(LocalDateTime lastRequest) { this.lastRequest = lastRequest; }
    }
}
