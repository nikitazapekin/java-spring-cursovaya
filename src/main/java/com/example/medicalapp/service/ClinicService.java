package com.example.medicalapp.service;

import com.example.medicalapp.entity.Child;
import com.example.medicalapp.entity.Clinic;
import com.example.medicalapp.models.ClinicResponse;
import com.example.medicalapp.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClinicService {

    @Autowired
    private ClinicRepository clinicRepository;

    public Optional<Clinic> findById(Long id) {
        return clinicRepository.findById(id);
    }

    public List<Clinic> findAll() {
        return (List<Clinic>) clinicRepository.findAll();
    }

    public List<Clinic> searchByName(String name) {
        return clinicRepository.findByNameContainingIgnoreCase(name);
    }

    public ClinicResponse convertToResponse(Clinic clinic) {
        ClinicResponse response = new ClinicResponse();
        response.setId(clinic.getId());
        response.setName(clinic.getName());
        response.setImagePath(clinic.getImagePath());
        response.setAddress(clinic.getAddress());
        response.setLatitude(clinic.getLatitude());
        response.setLongitude(clinic.getLongitude());
        return response;
    }

    public List<ClinicResponse> getAllClinicsResponse() {
        return findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ClinicResponse convertToResponseWithRegistration(Clinic clinic, Child child) {
        ClinicResponse response = convertToResponse(clinic);
        response.setRegistrationDate(child.getClinicRegistrationDate());
        return response;
    }
}

