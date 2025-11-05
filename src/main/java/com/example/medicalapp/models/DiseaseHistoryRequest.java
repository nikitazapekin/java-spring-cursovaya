
package com.example.medicalapp.models;

import java.time.LocalDate;

public class DiseaseHistoryRequest {
    private String diseaseName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public DiseaseHistoryRequest() {}


    public String getDiseaseName() { return diseaseName; }
    public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}