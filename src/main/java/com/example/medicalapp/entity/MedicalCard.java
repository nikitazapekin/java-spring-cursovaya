
package com.example.medicalapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medical_cards")
public class MedicalCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false, unique = true)
    private Child child;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "medicalCard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiseaseHistory> diseaseHistories = new ArrayList<>();

    @OneToMany(mappedBy = "medicalCard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicalTest> medicalTests = new ArrayList<>();

    @OneToMany(mappedBy = "medicalCard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicalAppointment> medicalAppointments = new ArrayList<>();

    public MedicalCard() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public MedicalCard(Child child) {
        this();
        this.child = child;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Child getChild() { return child; }
    public void setChild(Child child) { this.child = child; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<DiseaseHistory> getDiseaseHistories() { return diseaseHistories; }
    public void setDiseaseHistories(List<DiseaseHistory> diseaseHistories) { this.diseaseHistories = diseaseHistories; }

    public List<MedicalTest> getMedicalTests() { return medicalTests; }
    public void setMedicalTests(List<MedicalTest> medicalTests) { this.medicalTests = medicalTests; }

    public List<MedicalAppointment> getMedicalAppointments() { return medicalAppointments; }
    public void setMedicalAppointments(List<MedicalAppointment> medicalAppointments) { this.medicalAppointments = medicalAppointments; }
}