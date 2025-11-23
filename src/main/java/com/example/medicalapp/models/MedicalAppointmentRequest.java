package com.example.medicalapp.models;

public class MedicalAppointmentRequest {
    private Long medicalCardId;
    private Long doctorId;
    private Long serviceId;
    private String appointmentName;
    private String appointmentDate;
    private String appointmentTime;
    private String description;
    private String appointmentType;

    public MedicalAppointmentRequest() {}

    public Long getMedicalCardId() { return medicalCardId; }
    public void setMedicalCardId(Long medicalCardId) { this.medicalCardId = medicalCardId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public String getAppointmentName() { return appointmentName; }
    public void setAppointmentName(String appointmentName) { this.appointmentName = appointmentName; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }
}
