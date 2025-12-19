package com.example.medicalapp.controller;



import com.example.medicalapp.entity.Doctor;
import com.example.medicalapp.entity.User;
import com.example.medicalapp.models.DoctorResponse;
import com.example.medicalapp.models.DoctorUpdateRequest;
import com.example.medicalapp.models.MedicalAppointmentResponse;
import com.example.medicalapp.repository.DoctorRepository;
import com.example.medicalapp.repository.UserProfileRepository;
import com.example.medicalapp.service.DoctorService;
import com.example.medicalapp.service.MedicalAppointmentService;
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

    @Autowired
    private UserProfileRepository userRepository;
    
    @Autowired
    private MedicalAppointmentService medicalAppointmentService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentDoctor(HttpServletRequest request) {

        String userEmail = (String) request.getAttribute("userEmail");

        
        try {
            if (userEmail == null || userEmail.isEmpty()) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            if (userOpt.isEmpty()) {
                System.out.println("User not found with email: " + userEmail);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"User not found with email: " + userEmail + "\"}");
            }

            User user = userOpt.get();

            Optional<Doctor> doctorOpt = doctorRepository.findByUserId(user.getId());


            if (doctorOpt.isEmpty()) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Doctor profile not found for email: " + userEmail + "\"}");
            }

            Doctor doctor = doctorOpt.get();

            
            try {
            DoctorResponse response = mapToDoctorResponse(doctor);


            return ResponseEntity.ok(response);
            } catch (Exception mappingException) {
                System.out.println("ERROR mapping doctor to response: " + mappingException.getMessage());
                mappingException.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"message\": \"Error mapping doctor data: " + mappingException.getMessage() + "\"}");
            }
        } catch (Exception e) {
            System.out.println("ERROR retrieving doctor data: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving doctor data: " + e.getMessage() + "\"}");
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
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        
        DoctorResponse response = new DoctorResponse();
        response.setId(doctor.getId());
        response.setFirstName(doctor.getFirstName() != null ? doctor.getFirstName() : "");
        response.setMiddleName(doctor.getMiddleName());
        response.setLastName(doctor.getLastName() != null ? doctor.getLastName() : "");
        response.setRate(doctor.getRate() != null ? doctor.getRate() : 0.0);
        response.setStatus(doctor.getStatus());
        response.setCitate(doctor.getCitate());
        response.setExperience(doctor.getExperience() != null ? doctor.getExperience() : 0);
        response.setEducation(doctor.getEducation());
        response.setSpecialization(doctor.getSpecialization());
        response.setAchievements(doctor.getAchievements());
        response.setIncrementQualification(doctor.getIncrementQualification());
        response.setAvatar(doctor.getAvatar());
        response.setCreatedAt(doctor.getCreatedAt());
        
        if (doctor.getUser() != null) {
            response.setEmail(doctor.getUser().getEmail() != null ? doctor.getUser().getEmail() : "");
            response.setRole(doctor.getUser().getRole() != null ? doctor.getUser().getRole() : "");
        } else {
            System.out.println("WARNING: Doctor user is null for doctor ID: " + doctor.getId());
            response.setEmail("");
            response.setRole("");
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

    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentDoctor(
            HttpServletRequest request,
            @RequestBody DoctorUpdateRequest updateRequest) {
        try {
            String userEmail = (String) request.getAttribute("userEmail");
            System.out.println("Updating doctor profile for: " + userEmail);

            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"User not found\"}");
            }

            User user = userOpt.get();

            Optional<Doctor> doctorOpt = doctorRepository.findByUserId(user.getId());
            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Doctor profile not found\"}");
            }

            Doctor doctor = doctorOpt.get();

            if (updateRequest.getFirstName() != null) {
                doctor.setFirstName(updateRequest.getFirstName());
            }
            if (updateRequest.getMiddleName() != null) {
                doctor.setMiddleName(updateRequest.getMiddleName());
            }
            if (updateRequest.getLastName() != null) {
                doctor.setLastName(updateRequest.getLastName());
            }
            if (updateRequest.getSpecialization() != null) {
                doctor.setSpecialization(updateRequest.getSpecialization());
            }
            if (updateRequest.getEducation() != null) {
                doctor.setEducation(updateRequest.getEducation());
            }
            if (updateRequest.getIncrementQualification() != null) {
                doctor.setIncrementQualification(updateRequest.getIncrementQualification());
            }
            if (updateRequest.getExperience() != null) {
                doctor.setExperience(updateRequest.getExperience());
            }
            if (updateRequest.getAchievements() != null) {
                doctor.setAchievements(updateRequest.getAchievements());
            }
            if (updateRequest.getStatus() != null) {
                doctor.setStatus(updateRequest.getStatus());
            }
            if (updateRequest.getCitate() != null) {
                doctor.setCitate(updateRequest.getCitate());
            }
            if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(user.getEmail())) {

                if (userRepository.existsByEmail(updateRequest.getEmail())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("{\"message\": \"Email already exists\"}");
                }
                user.setEmail(updateRequest.getEmail());
                userRepository.save(user);
            }

            Doctor updatedDoctor = doctorRepository.save(doctor);
            DoctorResponse response = mapToDoctorResponse(updatedDoctor);

            System.out.println("Doctor profile updated successfully: " + response.getFirstName() + " " + response.getLastName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Error updating doctor profile: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error updating doctor profile: " + e.getMessage() + "\"}");
        }
    }
    
    @GetMapping("/appointments/today")
    public ResponseEntity<?> getTodayAppointments(HttpServletRequest request) {

        String userEmail = (String) request.getAttribute("userEmail");
        System.out.println("User email from request: " + userEmail);
        
        try {
            if (userEmail == null || userEmail.isEmpty()) {
                System.out.println("User email is null or empty");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            if (userOpt.isEmpty()) {
                System.out.println("User not found with email: " + userEmail);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"User not found\"}");
            }

            User user = userOpt.get();
            Optional<Doctor> doctorOpt = doctorRepository.findByUserId(user.getId());
            
            if (doctorOpt.isEmpty()) {
                System.out.println("Doctor profile not found for email: " + userEmail);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Doctor profile not found\"}");
            }

            Doctor doctor = doctorOpt.get();

            
            List<MedicalAppointmentResponse> appointments = 
                    medicalAppointmentService.getTodayAppointmentsByDoctorId(doctor.getId());
            
            System.out.println("Found " + appointments.size() + " appointments for today");
            return ResponseEntity.ok(appointments);
            
        } catch (Exception e) {
            System.out.println("ERROR retrieving today appointments: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving appointments: " + e.getMessage() + "\"}");
        }
    }
    
    @GetMapping("/appointments")
    public ResponseEntity<?> getAllAppointments(HttpServletRequest request) {

        String userEmail = (String) request.getAttribute("userEmail");
        
        try {
            if (userEmail == null || userEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"User not found\"}");
            }

            User user = userOpt.get();
            Optional<Doctor> doctorOpt = doctorRepository.findByUserId(user.getId());
            
            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Doctor profile not found\"}");
            }

            Doctor doctor = doctorOpt.get();
            List<MedicalAppointmentResponse> appointments = 
                    medicalAppointmentService.getAllAppointmentsByDoctorId(doctor.getId());
            
            return ResponseEntity.ok(appointments);
            
        } catch (Exception e) {
            System.out.println("ERROR retrieving appointments: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving appointments: " + e.getMessage() + "\"}");
        }
    }

}