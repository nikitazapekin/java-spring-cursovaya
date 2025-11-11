package com.example.medicalapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Chat() {
        this.createdAt = LocalDateTime.now();
    }

    public Chat(Patient patient, Doctor doctor, Long authorId) {
        this();
        this.patient = patient;
        this.doctor = doctor;
        this.authorId = authorId;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Вспомогательные методы для обратной совместимости
    public Long getPatientId() {
        return patient != null ? patient.getId() : null;
    }

    public Long getDoctorId() {
        return doctor != null ? doctor.getId() : null;
    }
}