package com.example.medicalapp.models;

import java.time.LocalDateTime;

public class RecommendationResponse {
    private Long id;
    private String category;
    private String title;
    private String date;
    private String fullDescription;
    private String imagePath;
    private LocalDateTime createdAt;

    public RecommendationResponse() {}

    public RecommendationResponse(Long id, String category, String title, String date, 
                                  String fullDescription, String imagePath, LocalDateTime createdAt) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.date = date;
        this.fullDescription = fullDescription;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getFullDescription() { return fullDescription; }
    public void setFullDescription(String fullDescription) { this.fullDescription = fullDescription; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

