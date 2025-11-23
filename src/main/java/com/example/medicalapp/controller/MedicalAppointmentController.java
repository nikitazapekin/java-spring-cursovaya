package com.example.medicalapp.controller;

import com.example.medicalapp.entity.MedicalAppointment;
import com.example.medicalapp.models.MedicalAppointmentRequest;
import com.example.medicalapp.models.MedicalAppointmentResponse;
import com.example.medicalapp.service.MedicalAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medical-appointments")
public class MedicalAppointmentController {

    @Autowired
    private MedicalAppointmentService medicalAppointmentService;

    @GetMapping("/medical-card/{medicalCardId}")
    public ResponseEntity<?> getAppointmentsByMedicalCardId(@PathVariable Long medicalCardId) {
        try {
            List<MedicalAppointmentResponse> appointments =
                    medicalAppointmentService.findAppointmentResponsesByMedicalCardId(medicalCardId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving appointments: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        try {
            Optional<MedicalAppointment> appointmentOpt = medicalAppointmentService.findById(id);
            if (appointmentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Appointment not found\"}");
            }

            MedicalAppointmentResponse response =
                    medicalAppointmentService.convertToResponse(appointmentOpt.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving appointment: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody MedicalAppointmentRequest request) {
        try {
            MedicalAppointment appointment = medicalAppointmentService.createAppointment(request);
            MedicalAppointmentResponse response =
                    medicalAppointmentService.convertToResponse(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"Error creating appointment: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        try {
            medicalAppointmentService.deleteAppointment(id);
            return ResponseEntity.ok("{\"message\": \"Appointment deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"Error deleting appointment: " + e.getMessage() + "\"}");
        }
    }
}

