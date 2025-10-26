package com.example.medicalapp.models;


public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String role;
    private String email;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String accessToken, String role, String email) {
        this.accessToken = accessToken;
        this.role = role;
        this.email = email;
    }

    public AuthResponse(String message) {
        this.message = message;
    }

     public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}