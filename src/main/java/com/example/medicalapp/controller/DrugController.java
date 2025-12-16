package com.example.medicalapp.controller;

import com.example.medicalapp.models.DrugRequest;
import com.example.medicalapp.models.DrugResponse;
import com.example.medicalapp.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drugs")
public class DrugController {

    @Autowired
    private DrugService drugService;

    @GetMapping
    public ResponseEntity<?> getAllDrugs(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy) {
        try {
            List<DrugResponse> drugs = drugService.findDrugsWithSearchAndSort(search, sortBy);
            return ResponseEntity.ok(drugs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving drugs: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDrugById(@PathVariable Long id) {
        try {
            var drugOpt = drugService.findById(id);
            if (drugOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Drug not found\"}");
            }
            DrugResponse response = drugService.convertToResponse(drugOpt.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving drug: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping
    public ResponseEntity<?> createDrug(@RequestBody DrugRequest drugRequest) {
        try {
            var createdDrug = drugService.createDrug(drugRequest);
            DrugResponse response = drugService.convertToResponse(createdDrug);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error creating drug: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDrug(
            @PathVariable Long id,
            @RequestBody DrugRequest drugRequest) {
        try {
            var updatedDrug = drugService.updateDrug(id, drugRequest);
            DrugResponse response = drugService.convertToResponse(updatedDrug);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error updating drug: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDrug(@PathVariable Long id) {
        try {
            if (!drugService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Drug not found\"}");
            }
            drugService.deleteById(id);
            return ResponseEntity.ok("{\"message\": \"Drug deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error deleting drug: " + e.getMessage() + "\"}");
        }
    }
}

