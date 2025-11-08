package com.example.medicalapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_messages")
public class UserMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private UserChats chat;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(nullable = false)
    private String from;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(name = "is_read")
    private Boolean isRead = false;

    public UserMessages() {
        this.time = LocalDateTime.now();
        this.isRead = false;
    }

    public UserMessages(UserChats chat, String message, String from) {
        this.chat = chat;
        this.message = message;
        this.from = from;
        this.time = LocalDateTime.now();
        this.isRead = false;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserChats getChat() { return chat; }
    public void setChat(UserChats chat) { this.chat = chat; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
}