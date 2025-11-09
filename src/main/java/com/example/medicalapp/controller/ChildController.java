package com.example.medicalapp.controller;


import com.example.medicalapp.entity.Child;
import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.models.ChildRequest;
import com.example.medicalapp.models.ChildResponse;
import com.example.medicalapp.service.ChildService;
import com.example.medicalapp.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/children")
public class ChildController {

    @Autowired
    private ChildService childService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<?> getChildrenByParentId(@PathVariable Long parentId) {
        try {
            List<ChildResponse> children = childService.getChildrenByParentId(parentId);
            return ResponseEntity.ok(children);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving children: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/my-children")
    public ResponseEntity<?> getMyChildren(HttpServletRequest request) {
        try {
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            List<ChildResponse> children = childService.getChildrenByParentEmail(userEmail);
            return ResponseEntity.ok(children);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving children: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChildById(@PathVariable Long id) {
        try {
            Optional<ChildResponse> childResponseOpt = childService.getChildById(id);
            if (childResponseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Child not found\"}");
            }
            return ResponseEntity.ok(childResponseOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving child: " + e.getMessage() + "\"}");
        }
    }


    @PostMapping
    public ResponseEntity<?> createChild(@RequestBody ChildRequest childRequest, HttpServletRequest request) {

        String userEmail = (String) request.getAttribute("userEmail");


        try {
            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            var currentPatientOpt = patientService.findByUserEmail(userEmail);
            if (currentPatientOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Patient profile not found\"}");
            }


            Long currentPatientId = currentPatientOpt.get().getId();
            System.out.println("Current patient ID: " + currentPatientId);

            ChildRequest validatedRequest = new ChildRequest();
            validatedRequest.setName(childRequest.getName());
            validatedRequest.setAge(childRequest.getAge());
            validatedRequest.setGender(childRequest.getGender());
            validatedRequest.setAvatar(childRequest.getAvatar());
            validatedRequest.setParentId(currentPatientId);

            ChildResponse createdChild = childService.createChild(validatedRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdChild);

        } catch (Exception e) {
            System.out.println("Error creating child: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error creating child: " + e.getMessage() + "\"}");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateChild(@PathVariable Long id, @RequestBody ChildRequest childRequest, HttpServletRequest request) {
        try {
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }

            var currentPatientOpt = patientService.findByUserEmail(userEmail);
            if (currentPatientOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Patient profile not found\"}");
            }

            Long currentPatientId = currentPatientOpt.get().getId();
            Optional<ChildResponse> existingChildOpt = childService.getChildByIdAndParentId(id, currentPatientId);
            if (existingChildOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Child not found or access denied\"}");
            }

            Optional<ChildResponse> updatedChildOpt = childService.updateChild(id, childRequest);
            if (updatedChildOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Child not found\"}");
            }

            return ResponseEntity.ok(updatedChildOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error updating child: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChild(@PathVariable Long id, HttpServletRequest request) {
        try {
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"User not authenticated\"}");
            }


            var currentPatientOpt = patientService.findByUserEmail(userEmail);
            if (currentPatientOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Patient profile not found\"}");
            }

            Long currentPatientId = currentPatientOpt.get().getId();
            boolean deleted = childService.deleteChildByParentId(id, currentPatientId);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Child not found or access denied\"}");
            }

            return ResponseEntity.ok("{\"message\": \"Child deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error deleting child: " + e.getMessage() + "\"}");
        }
    }



    @GetMapping("/{id}/full")
    public ResponseEntity<?> getChildFullInfoById(@PathVariable Long id) {
        try {
            System.out.println("=== GET CHILD FULL INFO ===");
            System.out.println("Requested child ID: " + id);

            Optional<ChildResponse> childResponseOpt = childService.getChildWithIdentifier(id);

            if (childResponseOpt.isEmpty()) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Child not found\"}");
            }

            ChildResponse childResponse = childResponseOpt.get();


            if (childResponse.getIdentifier() == null) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"message\": \"Child identifier not found\"}");
            }

            return ResponseEntity.ok(childResponse);

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving child information: " + e.getMessage() + "\"}");
        }
    }

}