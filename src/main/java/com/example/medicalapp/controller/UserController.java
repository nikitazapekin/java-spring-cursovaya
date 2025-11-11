package com.example.medicalapp.controller;

import com.example.medicalapp.models.UserInfoDTO;
import com.example.medicalapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/info")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable Long userId) {
        try {
            System.out.println("Received request for user info, ID: " + userId);
            UserInfoDTO userInfo = userService.getUserInfo(userId);
            System.out.println("Returning user info: " + userInfo.getFirstName() + " " + userInfo.getLastName());
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            System.out.println("Error getting user info for ID " + userId + ": " + e.getMessage());
            return ResponseEntity.notFound().build();
        }




    }
}