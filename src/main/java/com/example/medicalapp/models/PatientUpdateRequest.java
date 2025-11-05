package com.example.medicalapp.models;

public   class PatientUpdateRequest {
    private String firstName;
    private String lastName;
    private String region;
    private String phoneNumber;
    private String citate;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCitate() { return citate; }
    public void setCitate(String citate) { this.citate = citate; }
}