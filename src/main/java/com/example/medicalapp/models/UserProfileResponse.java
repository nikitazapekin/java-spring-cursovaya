package com.example.medicalapp.models;

import java.time.LocalDateTime;

public class UserProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String avatar;
    private LocalDateTime createdAt;

    // Общие поля
    private String phoneNumber;
    private String region;
    private String citate;

    // Специфичные для Doctor
    private String middleName;
    private Double rate;
    private String status;
    private Integer experience;
    private String education;
    private String specialization;
    private String achievements;
    private String incrementQualification;

    // Специфичные для Patient
    private String registrationDate;

    public UserProfileResponse() {}

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getCitate() { return citate; }
    public void setCitate(String citate) { this.citate = citate; }

    public Double getRate() { return rate; }
    public void setRate(Double rate) { this.rate = rate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getAchievements() { return achievements; }
    public void setAchievements(String achievements) { this.achievements = achievements; }

    public String getIncrementQualification() { return incrementQualification; }
    public void setIncrementQualification(String incrementQualification) { this.incrementQualification = incrementQualification; }

    public String getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }
}