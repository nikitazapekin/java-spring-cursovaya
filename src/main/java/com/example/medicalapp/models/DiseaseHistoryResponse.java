
package com.example.medicalapp.models;

import java.time.LocalDate;

public class DiseaseHistoryResponse {
    private Long id;
    private String diseaseName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public DiseaseHistoryResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDiseaseName() { return diseaseName; }
    public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}