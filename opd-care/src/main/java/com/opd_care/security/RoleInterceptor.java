package com.opd_care.security;

import com.opd_care.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    private static final String ROLE_HEADER = "X-User-Role";

    // Path -> Allowed Roles mapping
    private static final Map<String, List<String>> ROLE_MATRIX = Map.of(
        "/api/v1/patients", Arrays.asList("RECEPT", "ADMIN"),
        "/api/v1/appointments", Arrays.asList("RECEPT", "ADMIN"),
        "/api/v1/consultations", Arrays.asList("DOCTOR", "ADMIN")
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        String method = request.getMethod().toUpperCase();
        String role = request.getHeader(ROLE_HEADER);

        // Allow CORS preflight requests
        if ("OPTIONS".equals(method)) {
            return true;
        }

        // Public/Whitelisted paths
        if (path.contains("/auth/login") || path.contains("/error")) {
            return true;
        }

        // Enforce X-User-Role header presence
        if (role == null) {
            throw new UnauthorizedException("Header 'X-User-Role' is missing. Access denied.");
        }
        role = role.toUpperCase();

        // 1. Patients
        if (path.startsWith("/api/v1/patients")) {
            if (method.equals("POST") || method.equals("PUT")) {
                validateRole(role, Arrays.asList("RECEPT", "ADMIN"));
            } else if (method.equals("GET")) {
                validateRole(role, Arrays.asList("RECEPT", "DOCTOR", "ADMIN"));
            }
        }
        // 2. Appointments
        else if (path.startsWith("/api/v1/appointments")) {
            if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE")) {
                validateRole(role, Arrays.asList("RECEPT", "ADMIN"));
            } else if (method.equals("GET")) {
                validateRole(role, Arrays.asList("RECEPT", "DOCTOR", "ADMIN"));
            }
        }
        // 3. Consultations
        else if (path.startsWith("/api/v1/consultations")) {
            // Patient History is DOCTOR/ADMIN only
            if (path.matches(".*/consultations/[^/]+$") && method.equals("GET") && !path.contains("search")) {
                validateRole(role, Arrays.asList("DOCTOR", "ADMIN"));
            }
            else if (method.equals("POST") || method.equals("PUT")) {
                validateRole(role, Arrays.asList("DOCTOR", "ADMIN"));
            } else if (method.equals("GET")) {
                validateRole(role, Arrays.asList("RECEPT", "DOCTOR", "ADMIN"));
            }
        }

        return true;
    }

    private void validateRole(String userRole, List<String> allowedRoles) {
        if (!allowedRoles.contains(userRole)) {
            throw new UnauthorizedException("User role '" + userRole + "' is not authorized. Allowed: " + allowedRoles);
        }
    }
}
