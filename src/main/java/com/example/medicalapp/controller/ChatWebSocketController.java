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

        System.out.println("WebSocketMessage: " + webSocketMessage);

        try {
            MessageDTO savedMessage = chatService.sendMessage(
                    webSocketMessage.getChatId(),
                    webSocketMessage.getMessage(),
                    webSocketMessage.getSenderId(),
                    webSocketMessage.getReceiverId()
            );

            System.out.println("Message saved successfully: " + savedMessage);


            String destination = "/topic/chat/" + webSocketMessage.getChatId();
            System.out.println("Broadcasting message to: " + destination);
            messagingTemplate.convertAndSend(destination, savedMessage);



        } catch (Exception e) {
            System.out.println("ERROR sending message: " + e.getMessage());
            e.printStackTrace();

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

    @MessageMapping("/chat.startChat")
    public void startChat(@Payload WebSocketMessage webSocketMessage) {

        System.out.println("WebSocketMessage: " + webSocketMessage);

        try {
            if (webSocketMessage.getPatientId() == null || webSocketMessage.getDoctorId() == null) {
                throw new IllegalArgumentException("PatientId and DoctorId are required");
            }

            Long chatId = chatService.getOrCreateChat(
                    webSocketMessage.getPatientId(),
                    webSocketMessage.getDoctorId(),
                    webSocketMessage.getSenderId()
            );

            System.out.println("Chat created successfully with ID: " + chatId);

            WebSocketMessage response = new WebSocketMessage();
            response.setType("chat_started");
            response.setChatId(chatId);

            String userDestination = "/user/" + webSocketMessage.getSenderId() + "/queue/chat-started";
            System.out.println("Sending chat started notification to: " + userDestination);

            messagingTemplate.convertAndSendToUser(
                    webSocketMessage.getSenderId().toString(),
                    "/queue/chat-started",
                    response
            );

        } catch (Exception e) {
            System.out.println("ERROR starting chat: " + e.getMessage());
            e.printStackTrace();

            WebSocketMessage error = new WebSocketMessage();
            error.setType("error");
            error.setMessage("Failed to start chat: " + e.getMessage());

            messagingTemplate.convertAndSendToUser(
                    webSocketMessage.getSenderId().toString(),
                    "/queue/errors",
                    error
            );
        }
    }
}