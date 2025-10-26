package com.example.medicalapp.middleware;

import com.example.medicalapp.service.AuthService;
import com.example.medicalapp.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class DoctorAuthMiddleware implements HandlerInterceptor {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Authorization header required\"}");
            return false;
        }

        String token = authHeader.substring(7);

        if (!authService.validateAccessToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Invalid or expired access token\"}");
            return false;
        }


        if (!authService.isDoctor(token)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"message\": \"Doctor access required\"}");
            return false;
        }

        request.setAttribute("userEmail", jwtService.getEmailFromToken(token));
        request.setAttribute("userRole", jwtService.getRoleFromToken(token));
        request.setAttribute("userType", jwtService.getUserTypeFromToken(token));

        return true;
    }
}