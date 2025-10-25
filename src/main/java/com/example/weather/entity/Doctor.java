package com.example.weather.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(columnDefinition = "NUMERIC(3,2) DEFAULT 0.00")
    private Double rate;

    private String status;
    private String citate;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer experience;

    @Column(columnDefinition = "TEXT[]")
    private String[] education;

    private String specialization;
    private String achievements;

    @Column(name = "increment_qualification")
    private String incrementQualification;

    private String avatar;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public Doctor() {
        this.createdAt = LocalDateTime.now();
        this.rate = 0.00;
        this.experience = 0;
    }

    public Doctor(User user, String firstName, String lastName) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rate = 0.00;
        this.experience = 0;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Double getRate() { return rate; }
    public void setRate(Double rate) { this.rate = rate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCitate() { return citate; }
    public void setCitate(String citate) { this.citate = citate; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    public String[] getEducation() { return education; }
    public void setEducation(String[] education) { this.education = education; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getAchievements() { return achievements; }
    public void setAchievements(String achievements) { this.achievements = achievements; }

    public String getIncrementQualification() { return incrementQualification; }
    public void setIncrementQualification(String incrementQualification) { this.incrementQualification = incrementQualification; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}