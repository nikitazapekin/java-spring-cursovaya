package com.example.medicalapp.models;

import java.time.LocalDateTime;

public class MedicalAppointmentRequest {
    private String appointmentName;
    private LocalDateTime appointmentDate;
    private String description;
    private String appointmentType;
    private String doctorInitials;

    public MedicalAppointmentRequest() {}

    public String getAppointmentName() { return appointmentName; }
    public void setAppointmentName(String appointmentName) { this.appointmentName = appointmentName; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }

    public String getDoctorInitials() { return doctorInitials; }
    public void setDoctorInitials(String doctorInitials) { this.doctorInitials = doctorInitials; }
}