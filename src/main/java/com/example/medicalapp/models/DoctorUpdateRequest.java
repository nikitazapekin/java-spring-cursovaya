package com.example.medicalapp.models;

public class DoctorUpdateRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String specialization;
    private String education;
    private String incrementQualification;
    private Integer experience;
    private String achievements;
    private String status;
    private String citate;
    private String email;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getIncrementQualification() { return incrementQualification; }
    public void setIncrementQualification(String incrementQualification) { this.incrementQualification = incrementQualification; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    public String getAchievements() { return achievements; }
    public void setAchievements(String achievements) { this.achievements = achievements; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCitate() { return citate; }
    public void setCitate(String citate) { this.citate = citate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

