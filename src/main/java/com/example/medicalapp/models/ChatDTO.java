package com.example.medicalapp.models;

import java.time.LocalDateTime;

public class ChatDTO {
    private Long id;
    private String chatName;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private String avatar;
    private Long participantId;
    private String participantName;

    public ChatDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getChatName() { return chatName; }
    public void setChatName(String chatName) { this.chatName = chatName; }

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public LocalDateTime getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(LocalDateTime lastMessageTime) { this.lastMessageTime = lastMessageTime; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public Long getParticipantId() { return participantId; }
    public void setParticipantId(Long participantId) { this.participantId = participantId; }

    public String getParticipantName() { return participantName; }
    public void setParticipantName(String participantName) { this.participantName = participantName; }
}