
package com.example.medicalapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "medical_tests")
public class MedicalTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_card_id", nullable = false)
    private MedicalCard medicalCard;

    @Column(name = "test_name", nullable = false)
    private String testName;

    @Column(name = "test_date", nullable = false)
    private LocalDate testDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "doctor_name")
    private String doctorName;

    public MedicalTest() {}

    public MedicalTest(MedicalCard medicalCard, String testName, LocalDate testDate, String description, String doctorName) {
        this.medicalCard = medicalCard;
        this.testName = testName;
        this.testDate = testDate;
        this.description = description;
        this.doctorName = doctorName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MedicalCard getMedicalCard() { return medicalCard; }
    public void setMedicalCard(MedicalCard medicalCard) { this.medicalCard = medicalCard; }

    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }

    public LocalDate getTestDate() { return testDate; }
    public void setTestDate(LocalDate testDate) { this.testDate = testDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
}