
package com.example.medicalapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_messages")
public class DoctorMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private DoctorChats chat;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(nullable = false)
    private String sender;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(name = "is_read")
    private Boolean isRead = false;

    public DoctorMessages() {
        this.time = LocalDateTime.now();
        this.isRead = false;
    }

    public DoctorMessages(DoctorChats chat, String message, String sender, Long senderId) {
        this.chat = chat;
        this.message = message;
        this.sender = sender;
        this.senderId = senderId;
        this.time = LocalDateTime.now();
        this.isRead = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DoctorChats getChat() { return chat; }
    public void setChat(DoctorChats chat) { this.chat = chat; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
}