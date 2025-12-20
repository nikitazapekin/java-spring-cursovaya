package com.example.medicalapp.controller;

import com.example.medicalapp.models.AvatarResponse;
import com.example.medicalapp.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class AvatarController {

    @Autowired
    private AvatarService avatarService;

    @GetMapping("/{userId}/avatar")
    public ResponseEntity<?> getUserAvatar(@PathVariable Long userId) {
        try {
            Optional<AvatarResponse> avatarResponse = avatarService.getUserAvatar(userId);

            if (avatarResponse.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"User or avatar not found\", \"userId\": " + userId + "}");
            }

            return ResponseEntity.ok(avatarResponse.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving user avatar: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{userId}/avatar/url")
    public ResponseEntity<?> getAvatarUrl(@PathVariable Long userId) {
        try {
            String avatarUrl = avatarService.getAvatarUrl(userId);

            if (avatarUrl == null || avatarUrl.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Avatar URL not found\", \"userId\": " + userId + "}");
            }

            return ResponseEntity.ok("{\"avatarUrl\": \"" + avatarUrl + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving avatar URL: " + e.getMessage() + "\"}");
        }
    }
}