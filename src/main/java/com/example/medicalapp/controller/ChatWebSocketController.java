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
        System.out.println("=== Received message via WebSocket ===");
        System.out.println("WebSocketMessage: " + webSocketMessage);

        try {
            MessageDTO savedMessage;
            Long chatId = webSocketMessage.getChatId();

            System.out.println("Processing message. ChatId: " + chatId +
                    ", Type: " + webSocketMessage.getChatType() +
                    ", Sender: " + webSocketMessage.getSenderId() +
                    ", Message: " + webSocketMessage.getMessage());

            if ("user".equals(webSocketMessage.getChatType())) {
                System.out.println("Sending message to USER chat: " + chatId);
                savedMessage = chatService.sendMessageToUserChat(
                        chatId,
                        webSocketMessage.getMessage(),
                        webSocketMessage.getFrom(),
                        webSocketMessage.getSenderId()
                );
            } else {
                System.out.println("Sending message to DOCTOR chat: " + chatId);
                savedMessage = chatService.sendMessageToDoctorChat(
                        chatId,
                        webSocketMessage.getMessage(),
                        webSocketMessage.getFrom(),
                        webSocketMessage.getSenderId()
                );
            }

            System.out.println("Message saved successfully: " + savedMessage);

            String destination = "/topic/chat/" + chatId + "/" + webSocketMessage.getChatType();
            System.out.println("Broadcasting message to: " + destination);
            messagingTemplate.convertAndSend(destination, savedMessage);

            if (webSocketMessage.getReceiverId() != null) {
                String userDestination = "/user/" + webSocketMessage.getReceiverId() + "/queue/messages";
                System.out.println("Sending private message to: " + userDestination);
                messagingTemplate.convertAndSendToUser(
                        webSocketMessage.getReceiverId().toString(),
                        "/queue/messages",
                        savedMessage
                );
            }

            System.out.println("=== Message processing completed ===");

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
        System.out.println("=== Received chat start request ===");
        System.out.println("WebSocketMessage: " + webSocketMessage);

        try {
            if (webSocketMessage.getPatientId() == null || webSocketMessage.getDoctorId() == null) {
                throw new IllegalArgumentException("PatientId and DoctorId are required");
            }

            System.out.println("Creating chat for PatientId: " + webSocketMessage.getPatientId() +
                    ", DoctorId: " + webSocketMessage.getDoctorId() +
                    ", SenderId: " + webSocketMessage.getSenderId());

            Long chatId = chatService.getOrCreateUserChat(
                    webSocketMessage.getPatientId(),
                    webSocketMessage.getDoctorId()
            );

            System.out.println("Chat created successfully with ID: " + chatId);

            WebSocketMessage response = new WebSocketMessage();
            response.setType("chat_started");
            response.setChatId(chatId);
            response.setChatType("user");

            String userDestination = "/user/" + webSocketMessage.getSenderId() + "/queue/chat-started";
            System.out.println("Sending chat started notification to: " + userDestination);
            System.out.println("Response object: " + response);

            messagingTemplate.convertAndSendToUser(
                    webSocketMessage.getSenderId().toString(),
                    "/queue/chat-started",
                    response
            );

            System.out.println("=== Chat creation completed ===");

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