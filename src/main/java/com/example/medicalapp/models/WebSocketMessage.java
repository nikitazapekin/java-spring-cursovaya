package com.example.medicalapp.models;

public class WebSocketMessage {
    private String type;
    private Long chatId;
    private String chatType;
    private String message;
    private String from;
    private Long senderId;
    private Long receiverId;
    private Long patientId;
    private Long doctorId;

    public WebSocketMessage() {}

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public String getChatType() { return chatType; }
    public void setChatType(String chatType) { this.chatType = chatType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
}