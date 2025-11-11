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
    private Long patientId;
    private Long doctorId;
    private Long authorId;
    private LocalDateTime createdAt;

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

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}