package com.example.medicalapp.models;

import java.time.LocalDateTime;

public class ServiceResponse {
    private Long id;
    private String title;
    private String subtitle;
    private LocalDateTime createdAt;

    public ServiceResponse() {}

    public ServiceResponse(Long id, String title, String subtitle, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

