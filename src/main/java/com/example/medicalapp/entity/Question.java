package com.example.medicalapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT[]")
    private String[] questions;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Question() {
        this.createdAt = LocalDateTime.now();
    }

    public Question(Survey survey, String title, String[] questions, String image) {
        this.survey = survey;
        this.title = title;
        this.questions = questions;
        this.image = image;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Survey getSurvey() { return survey; }
    public void setSurvey(Survey survey) { this.survey = survey; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String[] getQuestions() { return questions; }
    public void setQuestions(String[] questions) { this.questions = questions; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}