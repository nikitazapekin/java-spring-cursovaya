package com.example.medicalapp.controller;

import com.example.medicalapp.models.PatientResponse;
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
}