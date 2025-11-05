
package com.example.medicalapp.models;

import java.time.LocalDateTime;
import java.util.List;

public class MedicalCardResponse {
    private Long id;
    private Long childId;
    private String childName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DiseaseHistoryResponse> diseaseHistories;
    private List<MedicalTestResponse> medicalTests;
    private List<MedicalAppointmentResponse> medicalAppointments;

    public MedicalCardResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getChildId() { return childId; }
    public void setChildId(Long childId) { this.childId = childId; }

    public String getChildName() { return childName; }
    public void setChildName(String childName) { this.childName = childName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<DiseaseHistoryResponse> getDiseaseHistories() { return diseaseHistories; }
    public void setDiseaseHistories(List<DiseaseHistoryResponse> diseaseHistories) { this.diseaseHistories = diseaseHistories; }

    public List<MedicalTestResponse> getMedicalTests() { return medicalTests; }
    public void setMedicalTests(List<MedicalTestResponse> medicalTests) { this.medicalTests = medicalTests; }

    public List<MedicalAppointmentResponse> getMedicalAppointments() { return medicalAppointments; }
    public void setMedicalAppointments(List<MedicalAppointmentResponse> medicalAppointments) { this.medicalAppointments = medicalAppointments; }
}