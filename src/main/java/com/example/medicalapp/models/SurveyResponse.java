package com.example.medicalapp.models;

import java.time.LocalDateTime;
import java.util.List;

public class SurveyResponse {
    private Long id;
    private String image;
    private String title;
    private String category;
    private LocalDateTime createdAt;
    private List<QuestionResponse> questions;

    public SurveyResponse() {}

    public SurveyResponse(Long id, String title, String category, String image) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.image = image;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<QuestionResponse> getQuestions() { return questions; }
    public void setQuestions(List<QuestionResponse> questions) { this.questions = questions; }
}