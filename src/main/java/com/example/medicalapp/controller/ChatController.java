package com.example.medicalapp.controller;

import com.example.medicalapp.models.ChatDTO;
import com.example.medicalapp.models.MessageDTO;
import com.example.medicalapp.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/user/{patientId}")
    public ResponseEntity<List<ChatDTO>> getUserChats(@PathVariable Long patientId) {
        List<ChatDTO> chats = chatService.getUserChats(patientId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ChatDTO>> getDoctorChats(@PathVariable Long doctorId) {
        List<ChatDTO> chats = chatService.getDoctorChats(doctorId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/user/{chatId}/messages")
    public ResponseEntity<List<MessageDTO>> getUserChatMessages(@PathVariable Long chatId) {
        List<MessageDTO> messages = chatService.getUserChatMessages(chatId);
        return ResponseEntity.ok(messages);
    }


    @GetMapping("/doctor/{chatId}/messages")
    public ResponseEntity<List<MessageDTO>> getDoctorChatMessages(@PathVariable Long chatId) {
        List<MessageDTO> messages = chatService.getDoctorChatMessages(chatId);
        return ResponseEntity.ok(messages);
    }


    @GetMapping("/my-chats")
    public ResponseEntity<List<ChatDTO>> getMyChats(@RequestParam Long userId, @RequestParam String userRole) {
        System.out.println("=== Getting chats for userId: " + userId + ", role: " + userRole + " ===");
        try {
            List<ChatDTO> chats;
            if ("DOCTOR".equals(userRole)) {
                chats = chatService.getDoctorChats(userId);
            } else {
                chats = chatService.getUserChats(userId);
            }
            System.out.println("Found " + chats.size() + " chats");
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            System.out.println("Error getting my chats: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/start")
    public ResponseEntity<Long> startChat(
            @RequestParam Long patientId,
            @RequestParam Long doctorId) {
        Long chatId = chatService.getOrCreateUserChat(patientId, doctorId);
        return ResponseEntity.ok(chatId);
    }
}