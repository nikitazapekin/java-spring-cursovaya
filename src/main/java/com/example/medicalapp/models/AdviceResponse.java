package com.example.medicalapp.models;

import java.time.LocalDateTime;

public class AdviceResponse {
    private Long id;
    private String type;
    private String items; // Теперь String вместо List<Map>
    private String[] recommendations;
    private LocalDateTime createdAt;

    public AdviceResponse() {}

    public AdviceResponse(Long id, String type, String items, String[] recommendations) {
        this.id = id;
        this.type = type;
        this.items = items;
        this.recommendations = recommendations;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }

    public String[] getRecommendations() { return recommendations; }
    public void setRecommendations(String[] recommendations) { this.recommendations = recommendations; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}