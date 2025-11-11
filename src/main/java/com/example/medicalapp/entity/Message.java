package com.example.medicalapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;


    @Column(name = "from_user", nullable = false)
    private Long fromUser;

    @Column(name = "to_user", nullable = false)
    private Long toUser;

    @Column(name = "message_text", nullable = false, columnDefinition = "TEXT")
    private String messageText;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "is_read")
    private Boolean isRead = false;

    public Message() {
        this.sentAt = LocalDateTime.now();
    }

    public Message(Chat chat, Long fromUser, Long toUser, String messageText) {
        this();
        this.chat = chat;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.messageText = messageText;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFromUser() { return fromUser; }
    public void setFromUser(Long fromUser) { this.fromUser = fromUser; }

    public Long getToUser() { return toUser; }
    public void setToUser(Long toUser) { this.toUser = toUser; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public Chat getChat() { return chat; }
    public void setChat(Chat chat) { this.chat = chat; }

    public Boolean getIsRead() {
        return  true;
    }
}