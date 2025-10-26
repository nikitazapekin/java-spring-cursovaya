package com.example.medicalapp.controller;

import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.models.PatientResponse;
import com.example.medicalapp.models.PatientUpdateRequest;
import com.example.medicalapp.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentPatient(HttpServletRequest request) {
        try {
            String userEmail = (String) request.getAttribute("userEmail");
            System.out.println("Looking for patient with email: " + userEmail);

            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            Optional<PatientResponse> patientResponseOpt = patientService.findPatientResponseByEmail(userEmail);

            if (patientResponseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Patient profile not found for email: " + userEmail + "\"}");
            }

            PatientResponse response = patientResponseOpt.get();
            System.out.println("Found patient: " + response.getFirstName() + " " + response.getLastName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error in getCurrentPatient: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving patient data: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        try {
            Optional<PatientResponse> patientResponseOpt = patientService.findPatientResponseById(id);

            if (patientResponseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Patient not found\"}");
            }

            return ResponseEntity.ok(patientResponseOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving patient data\"}");
        }
    }

    // Добавляем новый эндпоинт для обновления аватара
    @PutMapping("/updateAvatar")
    public ResponseEntity<?> updatePatientAvatar(HttpServletRequest request, @RequestBody AvatarUpdateRequest avatarRequest) {
        try {
            String userEmail = (String) request.getAttribute("userEmail");
            System.out.println("Updating avatar for user: " + userEmail);

            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            // Используем метод из сервиса
            Patient updatedPatient = patientService.updatePatientAvatar(userEmail, avatarRequest.getAvatar());
            PatientResponse response = patientService.convertToResponse(updatedPatient);

            System.out.println("Avatar updated successfully for patient: " + response.getFirstName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Error in updatePatientAvatar: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error updating avatar: " + e.getMessage() + "\"}");
        }
    }




    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentPatient(
            HttpServletRequest request,
            @RequestBody PatientUpdateRequest updateRequest) {
        try {
            String userEmail = (String) request.getAttribute("userEmail");
            System.out.println("Updating patient profile for: " + userEmail);

            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            Optional<Patient> patientOpt = patientService.findByUserEmail(userEmail);

            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Patient profile not found\"}");
            }

            Patient patient = patientOpt.get();


            if (updateRequest.getFirstName() != null) {
                patient.setFirstName(updateRequest.getFirstName());
            }
            if (updateRequest.getLastName() != null) {
                patient.setLastName(updateRequest.getLastName());
            }
            if (updateRequest.getRegion() != null) {
                patient.setRegion(updateRequest.getRegion());
            }
            if (updateRequest.getPhoneNumber() != null) {
                patient.setPhoneNumber(updateRequest.getPhoneNumber());
            }
            if (updateRequest.getCitate() != null) {
                patient.setCitate(updateRequest.getCitate());
            }

            Patient updatedPatient = patientService.save(patient);
            PatientResponse response = patientService.convertToResponse(updatedPatient);

            System.out.println("Patient profile updated successfully: " + response.getFirstName() + " " + response.getLastName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Error in updateCurrentPatient: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error updating patient profile: " + e.getMessage() + "\"}");
        }
    }



    public static class AvatarUpdateRequest {
        private String avatar;


        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }







}