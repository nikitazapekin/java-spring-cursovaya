package com.example.medicalapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_chats")
public class DoctorChats {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private ChatBase chatBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false, insertable = false, updatable = false)
    private Doctor doctor;

    @Column(name = "chat_name", nullable = false, length = 255)
    private String chatName;

    @Column(name = "last_message", length = 1000)
    private String lastMessage;

    @Column(name = "last_message_time")
    private LocalDateTime lastMessageTime;

    private String avatar;

    @Column(name = "patient_id", insertable = false, updatable = false)
    private Long patientId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public DoctorChats() {
        this.createdAt = LocalDateTime.now();
    }

    public DoctorChats(ChatBase chatBase, Doctor doctor, String chatName, String avatar) {
        this();
        this.id = chatBase.getId();
        this.chatBase = chatBase;
        this.doctor = doctor;
        this.chatName = chatName;
        this.avatar = avatar;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ChatBase getChatBase() { return chatBase; }
    public void setChatBase(ChatBase chatBase) { this.chatBase = chatBase; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public String getChatName() { return chatName; }
    public void setChatName(String chatName) { this.chatName = chatName; }

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public LocalDateTime getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(LocalDateTime lastMessageTime) { this.lastMessageTime = lastMessageTime; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}