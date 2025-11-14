package com.example.medicalapp.models;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private String message;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime time;
    private Boolean isRead;
    private Long chatId;

    private String senderFirstName;
    private String senderLastName;
    private String senderAvatar;
    private String senderType;

    public MessageDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public String getSenderFirstName() { return senderFirstName; }
    public void setSenderFirstName(String senderFirstName) { this.senderFirstName = senderFirstName; }

    public String getSenderLastName() { return senderLastName; }
    public void setSenderLastName(String senderLastName) { this.senderLastName = senderLastName; }

    public String getSenderAvatar() { return senderAvatar; }
    public void setSenderAvatar(String senderAvatar) { this.senderAvatar = senderAvatar; }

    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }
}