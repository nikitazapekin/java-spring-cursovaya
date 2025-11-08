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

    @GetMapping("/user/messages/{chatId}")
    public ResponseEntity<List<MessageDTO>> getUserChatMessages(@PathVariable Long chatId) {
        List<MessageDTO> messages = chatService.getUserChatMessages(chatId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/doctor/messages/{chatId}")
    public ResponseEntity<List<MessageDTO>> getDoctorChatMessages(@PathVariable Long chatId) {
        List<MessageDTO> messages = chatService.getDoctorChatMessages(chatId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/start")
    public ResponseEntity<Long> startChat(
            @RequestParam Long patientId,
            @RequestParam Long doctorId) {
        Long chatId = chatService.getOrCreateUserChat(patientId, doctorId);
        return ResponseEntity.ok(chatId);
    }
}