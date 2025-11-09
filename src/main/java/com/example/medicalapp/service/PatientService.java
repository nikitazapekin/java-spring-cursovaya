package com.example.medicalapp.service;

import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.models.PatientResponse;
import com.example.medicalapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> findAll() {
        return (List<Patient>) patientRepository.findAll();
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> findByUserEmail(String email) {
        return patientRepository.findByUserEmail(email);
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public void deleteById(Long id) {
        patientRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return patientRepository.existsById(id);
    }

    public PatientResponse convertToResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setFirstName(patient.getFirstName());
        response.setLastName(patient.getLastName());
        response.setPhoneNumber(patient.getPhoneNumber());
        response.setRegion(patient.getRegion());
        response.setAvatar(patient.getAvatar());
        response.setCitate(patient.getCitate());
        response.setRegistrationDate(patient.getRegistrationDate());
        response.setCreatedAt(patient.getCreatedAt());

        if (patient.getUser() != null) {
            response.setEmail(patient.getUser().getEmail());
            response.setRole(patient.getUser().getRole());
        }

        return response;
    }

    public Optional<PatientResponse> findPatientResponseById(Long id) {
        return patientRepository.findById(id)
                .map(this::convertToResponse);
    }

    public Optional<PatientResponse> findPatientResponseByEmail(String email) {
        return patientRepository.findByUserEmail(email)
                .map(this::convertToResponse);
    }

    public Patient updatePatientAvatar(String email, String avatarUrl) {
        Optional<Patient> patientOpt = patientRepository.findByUserEmail(email);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            patient.setAvatar(avatarUrl);
            return patientRepository.save(patient);
        }
        throw new RuntimeException("Patient not found for email: " + email);
    }



    public List<PatientResponse> searchPatientsByName(String searchTerm) {
        List<Patient> patients = patientRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm);

        return patients.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    public List<PatientResponse> searchPatientsByFirstName(String firstName) {
        List<Patient> patients = patientRepository.findByFirstNameContainingIgnoreCase(firstName);
        return patients.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<PatientResponse> searchPatientsByLastName(String lastName) {
        List<Patient> patients = patientRepository.findByLastNameContainingIgnoreCase(lastName);
        return patients.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


}