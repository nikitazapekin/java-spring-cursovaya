package com.example.medicalapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret:defaultSecretKeyForDevelopment123!}")
    private String jwtSecret;

    @Value("${jwt.access.expiration:1130000}")
    private Long accessExpiration;

    @Value("${jwt.refresh.expiration:33160000}")
    private Long refreshExpiration;

    public String generateAccessToken(String email, String role, String userType) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", email);
        payload.put("role", role);
        payload.put("userType", userType);
        payload.put("type", "access");
        payload.put("iat", System.currentTimeMillis());
        payload.put("exp", System.currentTimeMillis() + accessExpiration);

        return createToken(headers, payload);
    }

    public String generateRefreshToken(String email, String role, String userType) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", email);
        payload.put("role", role);
        payload.put("userType", userType);
        payload.put("type", "refresh");
        payload.put("iat", System.currentTimeMillis());
        payload.put("exp", System.currentTimeMillis() + refreshExpiration);

        return createToken(headers, payload);
    }

    public boolean isRefreshToken(String token) {
        try {
            return "refresh".equals(getTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccessToken(String token) {
        try {
            return "access".equals(getTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Long exp = getExpirationFromToken(token);
            return exp != null && exp < System.currentTimeMillis();
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isRefreshTokenValid(String token) {
        return validateToken(token) && isRefreshToken(token) && !isTokenExpired(token);
    }

    public boolean isAccessTokenValid(String token) {
        return validateToken(token) && isAccessToken(token) && !isTokenExpired(token);
    }

    public String getUserTypeFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadBase64 = parts[1];
            String payloadJson = base64Decode(payloadBase64);
            Map<String, Object> payload = jsonToMap(payloadJson);
            return (String) payload.get("userType");
        } catch (Exception e) {
            return null;
        }
    }

    // Остальные методы без изменений...
    private String createToken(Map<String, Object> headers, Map<String, Object> payload) {
        String headerJson = mapToJson(headers);
        String payloadJson = mapToJson(payload);

        String headerBase64 = base64Encode(headerJson);
        String payloadBase64 = base64Encode(payloadJson);

        String signature = createSignature(headerBase64 + "." + payloadBase64);

        return headerBase64 + "." + payloadBase64 + "." + signature;
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }

            String headerBase64 = parts[0];
            String payloadBase64 = parts[1];
            String signature = parts[2];

            String expectedSignature = createSignature(headerBase64 + "." + payloadBase64);
            if (!signature.equals(expectedSignature)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadBase64 = parts[1];
            String payloadJson = base64Decode(payloadBase64);
            Map<String, Object> payload = jsonToMap(payloadJson);
            return (String) payload.get("sub");
        } catch (Exception e) {
            return null;
        }
    }

    public String getRoleFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadBase64 = parts[1];
            String payloadJson = base64Decode(payloadBase64);
            Map<String, Object> payload = jsonToMap(payloadJson);
            return (String) payload.get("role");
        } catch (Exception e) {
            return null;
        }
    }

    public String getTokenType(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadBase64 = parts[1];
            String payloadJson = base64Decode(payloadBase64);
            Map<String, Object> payload = jsonToMap(payloadJson);
            return (String) payload.get("type");
        } catch (Exception e) {
            return null;
        }
    }

    public Long getExpirationFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadBase64 = parts[1];
            String payloadJson = base64Decode(payloadBase64);
            Map<String, Object> payload = jsonToMap(payloadJson);
            Object exp = payload.get("exp");
            if (exp instanceof Long) {
                return (Long) exp;
            } else if (exp instanceof Integer) {
                return ((Integer) exp).longValue();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String createSignature(String data) {
        try {
            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256.init(secretKey);
            byte[] signature = sha256.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return base64EncodeBytes(signature);
        } catch (Exception e) {
            throw new RuntimeException("Error creating signature", e);
        }
    }

    private String base64Encode(String data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    private String base64EncodeBytes(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private String base64Decode(String data) {
        byte[] decoded = Base64.getUrlDecoder().decode(data);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    private String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            json.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }
            first = false;
        }
        json.append("}");
        return json.toString();
    }

    private Map<String, Object> jsonToMap(String json) {
        Map<String, Object> map = new HashMap<>();
        json = json.trim().substring(1, json.length() - 1);
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim();

                if (value.startsWith("\"") && value.endsWith("\"")) {
                    map.put(key, value.substring(1, value.length() - 1));
                } else {
                    try {
                        if (value.contains(".")) {
                            map.put(key, Double.parseDouble(value));
                        } else {
                            map.put(key, Long.parseLong(value));
                        }
                    } catch (NumberFormatException e) {
                        map.put(key, value);
                    }
                }
            }
        }
        return map;
    }

    public Long getRefreshExpiration() {
        return refreshExpiration;
    }
}