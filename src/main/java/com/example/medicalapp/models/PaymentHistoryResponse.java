package com.example.medicalapp.models;

import java.time.LocalDateTime;

public class PaymentHistoryResponse {
    private Long id;
    private LocalDateTime date;
    private String title;
    private String description;
    private Double price;
    private String doctor;
    private Long patientId;
    private String patientName;
    private LocalDateTime createdAt;

    public PaymentHistoryResponse() {}

    public PaymentHistoryResponse(Long id, LocalDateTime date, String title, String description,
                                  Double price, String doctor, Long patientId, String patientName,
                                  LocalDateTime createdAt) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.description = description;
        this.price = price;
        this.doctor = doctor;
        this.patientId = patientId;
        this.patientName = patientName;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}