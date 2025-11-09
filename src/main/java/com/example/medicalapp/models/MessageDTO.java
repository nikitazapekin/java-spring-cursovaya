package com.example.medicalapp.models;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private String message;
    private String from;
    private Long senderId;
    private LocalDateTime time;
    private Boolean isRead;
    private Long chatId;
    private String type;

    public MessageDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}