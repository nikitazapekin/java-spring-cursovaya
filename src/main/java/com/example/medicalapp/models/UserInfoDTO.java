package com.example.medicalapp.models;

public class UserInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String avatar;
    private String email;
    private String role;

    public UserInfoDTO() {}

    public UserInfoDTO(Long id, String firstName, String lastName, String avatar, String email, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.email = email;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}