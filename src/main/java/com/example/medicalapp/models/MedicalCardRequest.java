
package com.example.medicalapp.models;

public class MedicalCardRequest {
    private Long childId;

    public MedicalCardRequest() {}

    public MedicalCardRequest(Long childId) {
        this.childId = childId;
    }

    public Long getChildId() { return childId; }
    public void setChildId(Long childId) { this.childId = childId; }
}