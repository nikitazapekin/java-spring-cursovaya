package com.example.medicalapp.models;

import java.time.LocalDateTime;

public class QuestionResponse {
    private Long id;
    private Long surveyId;
    private String title;
    private String[] questions;
    private String image;
    private LocalDateTime createdAt;

    public QuestionResponse() {}

    public QuestionResponse(Long id, Long surveyId, String title, String[] questions, String image) {
        this.id = id;
        this.surveyId = surveyId;
        this.title = title;
        this.questions = questions;
        this.image = image;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSurveyId() { return surveyId; }
    public void setSurveyId(Long surveyId) { this.surveyId = surveyId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String[] getQuestions() { return questions; }
    public void setQuestions(String[] questions) { this.questions = questions; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}