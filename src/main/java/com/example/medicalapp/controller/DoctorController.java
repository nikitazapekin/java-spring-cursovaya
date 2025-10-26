package com.example.medicalapp.controller;



import com.example.medicalapp.entity.Doctor;
import com.example.medicalapp.models.DoctorResponse;
import com.example.medicalapp.repository.DoctorRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentDoctor(HttpServletRequest request) {
        try {
            String userEmail = (String) request.getAttribute("userEmail");
            Optional<Doctor> doctorOpt = doctorRepository.findByUserEmail(userEmail);

            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Doctor profile not found\"}");
            }

            Doctor doctor = doctorOpt.get();
            DoctorResponse response = mapToDoctorResponse(doctor);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving doctor data\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        try {
            Optional<Doctor> doctorOpt = doctorRepository.findById(id);

            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Doctor not found\"}");
            }

            Doctor doctor = doctorOpt.get();
            DoctorResponse response = mapToDoctorResponse(doctor);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving doctor data\"}");
        }
    }

    private DoctorResponse mapToDoctorResponse(Doctor doctor) {
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
        response.setEmail(doctor.getUser().getEmail());
        response.setRole(doctor.getUser().getRole());

        return response;
    }
}