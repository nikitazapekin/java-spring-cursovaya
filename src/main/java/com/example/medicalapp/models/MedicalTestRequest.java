package com.example.medicalapp.models;

import java.time.LocalDate;

public class MedicalTestRequest {
    private String testName;
    private LocalDate testDate;
    private String description;
    private String doctorName;

    public MedicalTestRequest() {}

    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }

    public LocalDate getTestDate() { return testDate; }
    public void setTestDate(LocalDate testDate) { this.testDate = testDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
}