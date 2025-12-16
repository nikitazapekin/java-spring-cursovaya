package com.example.medicalapp.controller;

import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.models.FavouriteDrugRequest;
import com.example.medicalapp.models.FavouriteDrugResponse;
import com.example.medicalapp.service.FavouriteDrugService;
import com.example.medicalapp.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/favourite-drugs")
public class FavouriteDrugController {

    @Autowired
    private FavouriteDrugService favouriteDrugService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyFavouriteDrugs(HttpServletRequest request) {
        try {
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            Optional<Patient> patientOpt = patientService.findByUserEmail(userEmail);
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Patient profile not found\"}");
            }

            Long patientId = patientOpt.get().getId();
            List<FavouriteDrugResponse> drugs = favouriteDrugService.findFavouriteDrugResponsesByPatientId(patientId);

            return ResponseEntity.ok(drugs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving favourite drugs: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getFavouriteDrugsByPatientId(@PathVariable Long patientId) {
        try {
            List<FavouriteDrugResponse> drugs = favouriteDrugService.findFavouriteDrugResponsesByPatientId(patientId);
            return ResponseEntity.ok(drugs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving favourite drugs: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFavouriteDrugById(@PathVariable Long id) {
        try {
            Optional<FavouriteDrugResponse> drugOpt = favouriteDrugService.findFavouriteDrugResponseById(id);
            if (drugOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Favourite drug not found\"}");
            }
            return ResponseEntity.ok(drugOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving favourite drug: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping
    public ResponseEntity<?> createFavouriteDrug(
            HttpServletRequest request,
            @RequestBody FavouriteDrugRequest drugRequest) {
        try {
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            Optional<Patient> patientOpt = patientService.findByUserEmail(userEmail);
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Patient profile not found\"}");
            }

            Long patientId = patientOpt.get().getId();
            var createdDrug = favouriteDrugService.createFavouriteDrug(patientId, drugRequest);
            FavouriteDrugResponse response = favouriteDrugService.convertToResponse(createdDrug);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("already in favourites")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("{\"message\": \"" + e.getMessage() + "\"}");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error creating favourite drug: " + e.getMessage() + "\"}");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFavouriteDrug(@PathVariable Long id) {
        try {
            if (!favouriteDrugService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Favourite drug not found\"}");
            }

            favouriteDrugService.deleteById(id);
            return ResponseEntity.ok("{\"message\": \"Favourite drug deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error deleting favourite drug: " + e.getMessage() + "\"}");
        }
    }
}



