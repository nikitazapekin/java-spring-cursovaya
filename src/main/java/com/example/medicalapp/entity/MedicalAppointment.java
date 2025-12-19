
package com.example.medicalapp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(name = "category")
    private String category;

    @Column(name = "title")
    private String title;

    @Column(name = "duration")
    private String duration;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "patient_name")
    private String patientName;

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

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

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
}