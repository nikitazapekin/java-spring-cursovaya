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

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentDoctor(HttpServletRequest request) {
        System.out.println("RECEIVE ");
        String userEmail = (String) request.getAttribute("userEmail");
        System.out.println("email ");
        System.out.println(userEmail);
        try {

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
        response.setMiddleName(doctor.getMiddleName());
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

    @GetMapping("/search")
    public ResponseEntity<?> searchDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String middleName,
            @RequestParam(required = false) String specialization) {

        try {
            List<DoctorResponse> doctors;

            if (name != null && !name.trim().isEmpty()) {
                doctors = doctorService.searchDoctorsByName(name.trim());
            } else if (firstName != null && !firstName.trim().isEmpty()) {
                doctors = doctorService.searchDoctorsByFirstName(firstName.trim());
            } else if (lastName != null && !lastName.trim().isEmpty()) {
                doctors = doctorService.searchDoctorsByLastName(lastName.trim());
            } else if (middleName != null && !middleName.trim().isEmpty()) {
                doctors = doctorService.searchDoctorsByMiddleName(middleName.trim());
            } else if (specialization != null && !specialization.trim().isEmpty()) {
                doctors = doctorService.searchDoctorsBySpecialization(specialization.trim());
            } else {
                return ResponseEntity.badRequest()
                        .body("{\"message\": \"Please provide search parameters: name, firstName, lastName, middleName, or specialization\"}");
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
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving doctors: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularDoctors() {
        try {
            List<DoctorResponse> doctors = doctorService.getPopularDoctors();

            if (doctors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"No popular doctors found\"}");
            }

            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving popular doctors: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/popular/top3")
    public ResponseEntity<?> getTop3PopularDoctors() {
        try {
            List<DoctorResponse> doctors = doctorService.getTop3PopularDoctors();

            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving top popular doctors: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/by-service/{serviceId}")
    public ResponseEntity<?> getDoctorsByService(@PathVariable Long serviceId) {
        try {
            List<DoctorResponse> doctors = doctorService.getDoctorsByServiceId(serviceId);

            if (doctors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"No doctors found for this service\"}");
            }

            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving doctors by service: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/by-child/{childId}")
    public ResponseEntity<?> getDoctorsByChild(@PathVariable Long childId) {
        try {
            List<DoctorResponse> doctors = doctorService.getDoctorsByChildId(childId);

            if (doctors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"No doctors found for this child\"}");
            }

            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving doctors by child: " + e.getMessage() + "\"}");
        }
    }


}