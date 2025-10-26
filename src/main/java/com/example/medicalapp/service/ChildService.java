package com.example.medicalapp.service;


import com.example.medicalapp.entity.Child;
import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.models.ChildRequest;
import com.example.medicalapp.models.ChildResponse;
import com.example.medicalapp.repository.ChildRepository;
import com.example.medicalapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private PatientRepository patientRepository;

    public List<ChildResponse> getChildrenByParentId(Long parentId) {
        List<Child> children = childRepository.findByParentId(parentId);
        return children.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ChildResponse> getChildrenByParentEmail(String email) {
        List<Child> children = childRepository.findByParentUserEmail(email);
        return children.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<ChildResponse> getChildById(Long id) {
        return childRepository.findById(id)
                .map(this::convertToResponse);
    }

    public Optional<ChildResponse> getChildByIdAndParentId(Long id, Long parentId) {
        return childRepository.findByIdAndParentId(id, parentId)
                .map(this::convertToResponse);
    }

    public ChildResponse createChild(ChildRequest childRequest) {
        Optional<Patient> parentOpt = patientRepository.findById(childRequest.getParentId());
        if (parentOpt.isEmpty()) {
            throw new RuntimeException("Parent not found with id: " + childRequest.getParentId());
        }

        Child child = new Child();
        child.setName(childRequest.getName());
        child.setAge(childRequest.getAge());
        child.setGender(childRequest.getGender());
        child.setAvatar(childRequest.getAvatar());
        child.setParent(parentOpt.get());

        Child savedChild = childRepository.save(child);
        return convertToResponse(savedChild);
    }

    public Optional<ChildResponse> updateChild(Long id, ChildRequest childRequest) {
        return childRepository.findById(id)
                .map(child -> {
                    if (childRequest.getName() != null) {
                        child.setName(childRequest.getName());
                    }
                    if (childRequest.getAge() != null) {
                        child.setAge(childRequest.getAge());
                    }
                    if (childRequest.getGender() != null) {
                        child.setGender(childRequest.getGender());
                    }
                    if (childRequest.getAvatar() != null) {
                        child.setAvatar(childRequest.getAvatar());
                    }

                    Child updatedChild = childRepository.save(child);
                    return convertToResponse(updatedChild);
                });
    }

    public boolean deleteChild(Long id) {
        if (childRepository.existsById(id)) {
            childRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean deleteChildByParentId(Long id, Long parentId) {
        Optional<Child> childOpt = childRepository.findByIdAndParentId(id, parentId);
        if (childOpt.isPresent()) {
            childRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public ChildResponse convertToResponse(Child child) {
        ChildResponse response = new ChildResponse();
        response.setId(child.getId());
        response.setAvatar(child.getAvatar());
        response.setName(child.getName());
        response.setAge(child.getAge());
        response.setGender(child.getGender());
        response.setParentId(child.getParent().getId());
        response.setCreatedAt(child.getCreatedAt());
        response.setUpdatedAt(child.getUpdatedAt());
        return response;
    }
}