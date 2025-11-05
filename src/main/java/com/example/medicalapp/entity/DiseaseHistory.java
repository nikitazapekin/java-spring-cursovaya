
package com.example.medicalapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "disease_history")
public class DiseaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_card_id", nullable = false)
    private MedicalCard medicalCard;

    @Column(name = "disease_name", nullable = false)
    private String diseaseName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    public DiseaseHistory() {}

    public DiseaseHistory(MedicalCard medicalCard, String diseaseName, LocalDate startDate, LocalDate endDate, String description) {
        this.medicalCard = medicalCard;
        this.diseaseName = diseaseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MedicalCard getMedicalCard() { return medicalCard; }
    public void setMedicalCard(MedicalCard medicalCard) { this.medicalCard = medicalCard; }

    public String getDiseaseName() { return diseaseName; }
    public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}