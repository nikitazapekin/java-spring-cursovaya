package com.example.medicalapp.models;

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
}