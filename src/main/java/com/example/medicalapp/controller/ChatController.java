package com.example.medicalapp.controller;

import com.example.medicalapp.entity.Chat;
import com.example.medicalapp.models.ChatDTO;
import com.example.medicalapp.models.MessageDTO;
import com.example.medicalapp.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/start")
    public ResponseEntity<Long> startChat(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam Long authorId) {
        try {
            Long chatId = chatService.getOrCreateChat(patientId, doctorId, authorId);
            return ResponseEntity.ok(chatId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageDTO>> getChatHistory(@PathVariable Long chatId) {
        try {
            List<MessageDTO> messages = chatService.getChatHistory(chatId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatDTO>> getUserChats(@PathVariable Long userId) {
        try {
            List<ChatDTO> chats = chatService.getUserChats(userId);
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ChatDTO>> getDoctorChats(@PathVariable Long doctorId) {
        try {
            List<ChatDTO> chats = chatService.getDoctorChats(doctorId);
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}