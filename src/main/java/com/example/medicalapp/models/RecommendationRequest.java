package com.example.medicalapp.models;

public class RecommendationRequest {
    private String category;
    private String title;
    private String date;
    private String fullDescription;
    private String imagePath;

    public RecommendationRequest() {}

    public RecommendationRequest(String category, String title, String date, 
                                 String fullDescription, String imagePath) {
        this.category = category;
        this.title = title;
        this.date = date;
        this.fullDescription = fullDescription;
        this.imagePath = imagePath;
    }

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
}

