package com.example.medicalapp.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MedicalAppointmentResponse {
    private Long id;
    private String appointmentName;
    private LocalDateTime appointmentDate;
    private String appointmentTime;
    private String description;
    private String appointmentType;
    private String doctorInitials;
    private DoctorResponse doctor;
    private ServiceResponse service;

    private String status;
    private String category;
    private String title;
    private String duration;
    private BigDecimal price;
    private LocalDateTime completedAt;
    private String patientName;
    private Long childId;

    public MedicalAppointmentResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAppointmentName() { return appointmentName; }
    public void setAppointmentName(String appointmentName) { this.appointmentName = appointmentName; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }

    public String getDoctorInitials() { return doctorInitials; }
    public void setDoctorInitials(String doctorInitials) { this.doctorInitials = doctorInitials; }

    public DoctorResponse getDoctor() { return doctor; }
    public void setDoctor(DoctorResponse doctor) { this.doctor = doctor; }

    public ServiceResponse getService() { return service; }
    public void setService(ServiceResponse service) { this.service = service; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Long getChildId() { return childId; }
    public void setChildId(Long childId) { this.childId = childId; }
}