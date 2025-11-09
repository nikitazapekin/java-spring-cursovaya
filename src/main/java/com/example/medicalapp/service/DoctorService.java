package com.example.medicalapp.service;

import com.example.medicalapp.entity.Doctor;
import com.example.medicalapp.models.DoctorResponse;
import com.example.medicalapp.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public Optional<Doctor> findById(Long id) {
        return doctorRepository.findById(id);
    }

    public Optional<Doctor> findByUserEmail(String email) {
        return doctorRepository.findByUserEmail(email);
    }

    public DoctorResponse convertToResponse(Doctor doctor) {
        DoctorResponse response = new DoctorResponse();
        response.setId(doctor.getId());
        response.setFirstName(doctor.getFirstName());
        response.setLastName(doctor.getLastName());
        response.setRate(doctor.getRate());
        response.setStatus(doctor.getStatus());
        response.setCitate(doctor.getCitate());
        response.setExperience(doctor.getExperience());
        response.setEducation(doctor.getEducation());
        response.setSpecialization(doctor.getSpecialization());
        response.setAchievements(doctor.getAchievements());
        response.setIncrementQualification(doctor.getIncrementQualification());
        response.setAvatar(doctor.getAvatar());
        response.setCreatedAt(doctor.getCreatedAt());

        if (doctor.getUser() != null) {
            response.setEmail(doctor.getUser().getEmail());
            response.setRole(doctor.getUser().getRole());
        }

        return response;
    }

    public List<DoctorResponse> searchDoctorsByName(String searchTerm) {
        List<Doctor> doctors = doctorRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm);

        return doctors.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<DoctorResponse> searchDoctorsByFirstName(String firstName) {
        List<Doctor> doctors = doctorRepository.findByFirstNameContainingIgnoreCase(firstName);
        return doctors.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<DoctorResponse> searchDoctorsByLastName(String lastName) {
        List<Doctor> doctors = doctorRepository.findByLastNameContainingIgnoreCase(lastName);
        return doctors.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    public List<DoctorResponse> searchDoctorsBySpecialization(String specialization) {
        List<Doctor> doctors = (List<Doctor>) doctorRepository.findAll();
        return doctors.stream()
                .filter(doctor -> doctor.getSpecialization() != null &&
                        doctor.getSpecialization().toLowerCase().contains(specialization.toLowerCase()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}