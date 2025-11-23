
package com.example.medicalapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_appointments")
public class MedicalAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_card_id", nullable = false)
    private MedicalCard medicalCard;

    @Column(name = "appointment_name", nullable = false)
    private String appointmentName;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "appointment_type")
    private String appointmentType;

    @Column(name = "doctor_initials")
    private String doctorInitials;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "appointment_time")
    private String appointmentTime;

    public MedicalAppointment() {}

    public MedicalAppointment(MedicalCard medicalCard, String appointmentName, LocalDateTime appointmentDate, String description, String appointmentType, String doctorInitials) {
        this.medicalCard = medicalCard;
        this.appointmentName = appointmentName;
        this.appointmentDate = appointmentDate;
        this.description = description;
        this.appointmentType = appointmentType;
        this.doctorInitials = doctorInitials;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MedicalCard getMedicalCard() { return medicalCard; }
    public void setMedicalCard(MedicalCard medicalCard) { this.medicalCard = medicalCard; }

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

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }
}