package com.example.medicalapp.models;



import java.time.LocalDateTime;

public class ChildResponse {
    private Long id;
    private String avatar;
    private String name;
    private Integer age;
    private String gender;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ChildResponse() {}

    public ChildResponse(Long id, String avatar, String name, Integer age, String gender, Long parentId) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.parentId = parentId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}