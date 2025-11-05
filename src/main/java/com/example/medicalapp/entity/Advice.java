package com.example.medicalapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "advice")
public class Advice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String items;

    @Column(columnDefinition = "TEXT[]")
    private String[] recommendations;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Advice() {
        this.createdAt = LocalDateTime.now();
    }

    public Advice(String type, String items, String[] recommendations) {
        this.type = type;
        this.items = items;
        this.recommendations = recommendations;
        this.createdAt = LocalDateTime.now();
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