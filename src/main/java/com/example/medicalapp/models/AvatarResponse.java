package com.example.medicalapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvatarResponse {
    private Long userId;
    private String avatar;
    private String userType;
    private String fullName;
    private String email;

    public AvatarResponse() {}

    public AvatarResponse(Long userId, String avatar, String userType, String fullName, String email) {
        this.userId = userId;
        this.avatar = avatar;
        this.userType = userType;
        this.fullName = fullName;
        this.email = email;
    }

    @JsonProperty("userId")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @JsonProperty("avatar")
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("userType")
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @JsonProperty("fullName")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}