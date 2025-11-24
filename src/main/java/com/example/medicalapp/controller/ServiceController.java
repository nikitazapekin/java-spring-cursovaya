package com.example.medicalapp.controller;

import com.example.medicalapp.models.ServiceResponse;
import com.example.medicalapp.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public ResponseEntity<?> getAllServices() {
        try {
            List<ServiceResponse> services = serviceService.findAllServiceResponses();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving services: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable Long id) {
        try {
            return serviceService.findById(id)
                    .map(service -> ResponseEntity.ok(serviceService.convertToResponse(service)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving service: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/top3")
    public ResponseEntity<?> getTop3Services() {
        try {
            List<ServiceResponse> services = serviceService.getTop3Services();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving top services: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedServices(@RequestParam(required = false, defaultValue = "title") String sortBy) {
        try {
            List<ServiceResponse> services;
            
            if ("popular".equalsIgnoreCase(sortBy)) {
                services = serviceService.getAllServicesSortedByPopularity();
            } else {
                services = serviceService.getAllServicesSortedByTitle();
            }
            
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving sorted services: " + e.getMessage() + "\"}");
        }
    }
}

