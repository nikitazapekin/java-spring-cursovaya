package com.example.medicalapp.models;

import java.time.LocalDateTime;

public class PaymentHistoryRequest {
    private LocalDateTime date;
    private String title;
    private String description;
    private Double price;
    private String doctor;
    private Long patientId;

    public PaymentHistoryRequest() {}

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
}