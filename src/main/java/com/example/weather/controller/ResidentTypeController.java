package com.example.weather.controller;


import com.example.weather.entity.ResidentType;
import com.example.weather.service.ResidentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/resident-types")
@CrossOrigin(origins = "*")
public class ResidentTypeController {

    @Autowired
    private ResidentTypeService residentTypeService;

    @GetMapping
    public ResponseEntity<List<ResidentType>> getAllResidentTypes() {
        try {
            List<ResidentType> residentTypes = residentTypeService.findAll();
            return ResponseEntity.ok(residentTypes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResidentType> getResidentTypeById(@PathVariable Long id) {
        try {
            Optional<ResidentType> residentType = residentTypeService.findById(id);
            return residentType.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createResidentType(@RequestBody ResidentType residentType) {
        try {
            if (residentType.getName() == null || residentType.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Resident type name is required");
            }
            if (residentTypeService.existsByName(residentType.getName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Resident type with name '" + residentType.getName() + "' already exists");
            }

            ResidentType savedResidentType = residentTypeService.save(residentType);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedResidentType);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating resident type: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResidentType(@PathVariable Long id,
                                                @RequestBody ResidentType residentTypeDetails) {
        try {
            Optional<ResidentType> existingResidentType = residentTypeService.findById(id);
            if (existingResidentType.isPresent()) {
                ResidentType residentType = existingResidentType.get();
                residentType.setName(residentTypeDetails.getName());
                residentType.setCommunicationLanguage(residentTypeDetails.getCommunicationLanguage());

                ResidentType updatedResidentType = residentTypeService.save(residentType);
                return ResponseEntity.ok(updatedResidentType);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating resident type: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResidentType(@PathVariable Long id) {
        try {
            if (residentTypeService.findById(id).isPresent()) {
                residentTypeService.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<List<ResidentType>> getResidentTypesByLanguage(@PathVariable String language) {
        try {
            List<ResidentType> residentTypes = residentTypeService.findByCommunicationLanguage(language);
            return ResponseEntity.ok(residentTypes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}