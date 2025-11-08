package com.example.medicalapp.controller;



import com.example.medicalapp.entity.Doctor;
import com.example.medicalapp.models.DoctorResponse;
import com.example.medicalapp.repository.DoctorRepository;
import com.example.medicalapp.service.DoctorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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



    @Autowired
    private DoctorService doctorService;
    @GetMapping("/search")
    public ResponseEntity<?> searchDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String specialization) {

        try {
            List<DoctorResponse> doctors;

            if (name != null && !name.trim().isEmpty()) {

                doctors = doctorService.searchDoctorsByName(name.trim());
            } else if (firstName != null && !firstName.trim().isEmpty()) {

                doctors = doctorService.searchDoctorsByFirstName(firstName.trim());
            } else if (lastName != null && !lastName.trim().isEmpty()) {

                doctors = doctorService.searchDoctorsByLastName(lastName.trim());
            } else if (specialization != null && !specialization.trim().isEmpty()) {

                doctors = doctorService.searchDoctorsBySpecialization(specialization.trim());
            } else {
                return ResponseEntity.badRequest()
                        .body("{\"message\": \"Please provide search parameters: name, firstName, lastName, or specialization\"}");
            }

            if (doctors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"No doctors found matching the search criteria\"}");
            }

            return ResponseEntity.ok(doctors);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error searching doctors: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllDoctors() {
        try {
            List<Doctor> doctors = (List<Doctor>) doctorRepository.findAll();
            List<DoctorResponse> response = doctors.stream()
                    .map(this::mapToDoctorResponse)
                    .collect(java.util.stream.Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving doctors\"}");
        }
    }


}