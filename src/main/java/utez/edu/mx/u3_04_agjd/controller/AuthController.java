package utez.edu.mx.u3_04_agjd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", authentication.getName());
        userInfo.put("authorities", authentication.getAuthorities());
        userInfo.put("authenticated", authentication.isAuthenticated());

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getAuthStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, String> status = new HashMap<>();
        status.put("status", authentication.isAuthenticated() ? "authenticated" : "not authenticated");
        status.put("user", authentication.getName());

        return ResponseEntity.ok(status);
    }
}
