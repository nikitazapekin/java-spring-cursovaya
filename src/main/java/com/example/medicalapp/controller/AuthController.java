package com.example.medicalapp.controller;

import com.example.medicalapp.models.AuthRequest;
import com.example.medicalapp.models.AuthResponse;
import com.example.medicalapp.models.RegisterRequest;
import com.example.medicalapp.service.AuthService;
import com.example.medicalapp.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request,
                                                 HttpServletResponse response) {
        AuthResponse authResponse = authService.register(request);

        System.out.println("REGISTERRRRRRRRRRRRRRRRRR");

        if (authResponse.getMessage() != null && authResponse.getMessage().equals("Email already exists")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(authResponse);
        }

        // Получаем роль из ответа и определяем тип пользователя
        String role = authResponse.getRole();
        String userType = "DOCTOR".equalsIgnoreCase(role) ? "DOCTOR" : "PATIENT";

        // Генерируем refresh token с правильными параметрами
        String refreshToken = jwtService.generateRefreshToken(request.getEmail(), role, userType);
        setRefreshTokenCookie(response, refreshToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request,
                                              HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);
        System.out.println("LOGINNNNNNNNn");

        if (authResponse.getMessage() != null && authResponse.getMessage().equals("Invalid email or password")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

        // Получаем роль из ответа и определяем тип пользователя
        String role = authResponse.getRole();
        String userType = "DOCTOR".equalsIgnoreCase(role) ? "DOCTOR" : "PATIENT";

        // Генерируем refresh token с правильными параметрами
        String refreshToken = jwtService.generateRefreshToken(request.getEmail(), role, userType);
        setRefreshTokenCookie(response, refreshToken);

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshAuth(HttpServletRequest request,
                                                    HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("No refresh token"));
        }

        AuthResponse authResponse = authService.refreshToken(refreshToken);

        if (authResponse.getMessage() != null) {
            // Если refresh token невалиден, очищаем cookie
            clearRefreshTokenCookie(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response) {
        // Просто очищаем refresh token cookie
        clearRefreshTokenCookie(response);
        return ResponseEntity.ok(new AuthResponse("Logged out successfully"));
    }

    @GetMapping("/validate")
    public ResponseEntity<AuthResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid authorization header"));
        }

        String accessToken = authHeader.substring(7);
        boolean isValid = authService.validateAccessToken(accessToken);

        if (isValid) {
            String email = jwtService.getEmailFromToken(accessToken);
            String role = jwtService.getRoleFromToken(accessToken);
            return ResponseEntity.ok(new AuthResponse("Token is valid", role, email));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid token"));
        }
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // Для локальной разработки false, в production - true
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (jwtService.getRefreshExpiration() / 1000)); // Convert to seconds
        refreshTokenCookie.setAttribute("SameSite", "Lax"); // Используем Lax вместо None для локальной разработки

        response.addCookie(refreshTokenCookie);
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", "");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setAttribute("SameSite", "Lax");

        response.addCookie(refreshTokenCookie);
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}