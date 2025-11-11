package com.example.medicalapp.controller;

import com.example.medicalapp.models.UserProfileResponse;
import com.example.medicalapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfileById(@PathVariable Long id) {
        try {
            System.out.println("Received request for user profile with ID: " + id);
            UserProfileResponse userProfile = userProfileService.getUserProfileById(id);
            return ResponseEntity.ok(userProfile);
        } catch (RuntimeException e) {
            System.out.println("Error getting user profile: " + e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"" + e.getMessage() + "\"}");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            System.out.println("Unexpected error getting user profile: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving user profile\"}");
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserProfileByEmail(@PathVariable String email) {
        try {
            System.out.println("Received request for user profile with email: " + email);
            UserProfileResponse userProfile = userProfileService.getUserProfileByEmail(email);
            return ResponseEntity.ok(userProfile);
        } catch (RuntimeException e) {
            System.out.println("Error getting user profile: " + e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"" + e.getMessage() + "\"}");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            System.out.println("Unexpected error getting user profile: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving user profile\"}");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserProfile(jakarta.servlet.http.HttpServletRequest request) {
        try {
            String userEmail = (String) request.getAttribute("userEmail");
            System.out.println("Looking for current user with email: " + userEmail);

            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            UserProfileResponse userProfile = userProfileService.getUserProfileByEmail(userEmail);
            System.out.println("Found user: " + userProfile.getFirstName() + " " + userProfile.getLastName());
            return ResponseEntity.ok(userProfile);
        } catch (RuntimeException e) {
            System.out.println("Error getting current user profile: " + e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"" + e.getMessage() + "\"}");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            System.out.println("Unexpected error getting current user profile: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving user profile\"}");
        }
    }
}