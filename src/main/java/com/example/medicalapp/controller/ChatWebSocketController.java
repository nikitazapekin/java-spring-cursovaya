package com.example.medicalapp.controller;

import com.example.medicalapp.models.MessageDTO;
import com.example.medicalapp.models.WebSocketMessage;
import com.example.medicalapp.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload WebSocketMessage webSocketMessage) {
        try {
            MessageDTO savedMessage;

            if ("user".equals(webSocketMessage.getChatType())) {
                savedMessage = chatService.sendUserMessage(
                        webSocketMessage.getChatId(),
                        webSocketMessage.getMessage(),
                        webSocketMessage.getFrom()
                );
            } else {
                savedMessage = chatService.sendDoctorMessage(
                        webSocketMessage.getChatId(),
                        webSocketMessage.getMessage(),
                        webSocketMessage.getFrom()
                );
            }

            // Send to specific chat
            messagingTemplate.convertAndSend(
                    "/topic/chat/" + webSocketMessage.getChatId() + "/" + webSocketMessage.getChatType(),
                    savedMessage
            );

            // Notify about new message to participants
            WebSocketMessage notification = new WebSocketMessage();
            notification.setType("new_message");
            notification.setChatId(webSocketMessage.getChatId());
            notification.setChatType(webSocketMessage.getChatType());

            // Send to patient if doctor sent message and vice versa
            if ("user".equals(webSocketMessage.getChatType())) {
                messagingTemplate.convertAndSendToUser(
                        webSocketMessage.getReceiverId().toString(),
                        "/queue/messages",
                        notification
                );
            } else {
                messagingTemplate.convertAndSendToUser(
                        webSocketMessage.getReceiverId().toString(),
                        "/queue/messages",
                        notification
                );
            }

        } catch (Exception e) {
            // Send error back to sender
            WebSocketMessage error = new WebSocketMessage();
            error.setType("error");
            error.setMessage("Failed to send message: " + e.getMessage());
            messagingTemplate.convertAndSendToUser(
                    webSocketMessage.getSenderId().toString(),
                    "/queue/errors",
                    error
            );
        }
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload WebSocketMessage webSocketMessage) {
        // Notify that user joined the chat
        WebSocketMessage joinMessage = new WebSocketMessage();
        joinMessage.setType("join");
        joinMessage.setChatId(webSocketMessage.getChatId());
        joinMessage.setFrom(webSocketMessage.getFrom());

        messagingTemplate.convertAndSend(
                "/topic/chat/" + webSocketMessage.getChatId() + "/" + webSocketMessage.getChatType(),
                joinMessage
        );
    }
}