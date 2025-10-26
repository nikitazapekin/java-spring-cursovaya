package com.example.medicalapp.models;



public class ChildRequest {
    private String avatar;
    private String name;
    private Integer age;
    private String gender;
    private Long parentId;

    // Constructors
    public ChildRequest() {}

    public ChildRequest(String name, Integer age, String gender, Long parentId) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.parentId = parentId;
    }

    // Getters and Setters
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
}