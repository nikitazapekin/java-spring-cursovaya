package com.example.medicalapp.controller;

import com.example.medicalapp.entity.Child;
import com.example.medicalapp.entity.Clinic;
import com.example.medicalapp.models.ClinicResponse;
import com.example.medicalapp.repository.ChildRepository;
import com.example.medicalapp.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clinics")
public class ClinicController {

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private ChildRepository childRepository;

    @GetMapping
    public ResponseEntity<?> getAllClinics() {
        try {
            List<ClinicResponse> clinics = clinicService.getAllClinicsResponse();
            return ResponseEntity.ok(clinics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving clinics: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClinicById(@PathVariable Long id) {
        try {
            Optional<Clinic> clinicOpt = clinicService.findById(id);
            
            if (clinicOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Clinic not found\"}");
            }

            ClinicResponse response = clinicService.convertToResponse(clinicOpt.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving clinic: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/by-child/{childId}")
    public ResponseEntity<?> getClinicByChildId(@PathVariable Long childId) {
        try {
            Optional<Child> childOpt = childRepository.findById(childId);
            
            if (childOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Child not found\"}");
            }

            Child child = childOpt.get();
            if (child.getClinic() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Clinic not assigned to this child\"}");
            }

            ClinicResponse response = clinicService.convertToResponseWithRegistration(child.getClinic(), child);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving clinic by child: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchClinics(@RequestParam String name) {
        try {
            List<Clinic> clinics = clinicService.searchByName(name);
            
            if (clinics.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"No clinics found matching the search criteria\"}");
            }

            List<ClinicResponse> response = clinics.stream()
                    .map(clinicService::convertToResponse)
                    .toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error searching clinics: " + e.getMessage() + "\"}");
        }
    }
}

