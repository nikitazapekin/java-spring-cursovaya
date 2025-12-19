package com.example.medicalapp.controller;

import com.example.medicalapp.entity.MedicalAppointment;
import com.example.medicalapp.models.MedicalAppointmentRequest;
import com.example.medicalapp.models.MedicalAppointmentResponse;
import com.example.medicalapp.models.MedicalTestResponse;
import com.example.medicalapp.service.MedicalAppointmentService;
import com.example.medicalapp.service.MedicalCardService;
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
    private MedicalCardService medicalCardService;


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



    @GetMapping("/{medicalCardId}/analyzes/by-date")
    public ResponseEntity<?> getAnalyzesByDate(
            @PathVariable Long medicalCardId,
            @RequestParam String date) {
        try {
            List<MedicalTestResponse> analyzes =
                    medicalCardService.getMedicalTestsByDate(medicalCardId, date);
            return ResponseEntity.ok(analyzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving analyzes by date: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/medical-card/{medicalCardId}/analyzes")

    public ResponseEntity<?> getAnalyzesByMedicalCardId(@PathVariable Long medicalCardId) {

        System.out.println("STARRRRRRRRRRRRRRRRRRRRRRRRRRRRTTTTTTTTTTTTTTTTTTTTTTTTTTTTT: " + medicalCardId);

        try {


            List<MedicalAppointmentResponse> analyzes =
                    medicalAppointmentService.findAnalyzesByMedicalCardId(medicalCardId);

            System.out.println("Total analyzes found: " + analyzes.size());

            return ResponseEntity.ok(analyzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving analyzes: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/medical-card/{medicalCardId}/analyzes/by-date")
    public ResponseEntity<?> getAnalyzesByMedicalCardIdAndDate(
            @PathVariable Long medicalCardId,
            @RequestParam String date) {



        try {
            List<MedicalAppointmentResponse> analyzes =
                    medicalAppointmentService.findAnalyzesByMedicalCardIdAndDate(medicalCardId, date);
            return ResponseEntity.ok(analyzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving analyzes by date: " + e.getMessage() + "\"}");
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

    @GetMapping("/consultations/patient/{patientId}")
    public ResponseEntity<?> getConsultationHistory(
            @PathVariable Long patientId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, defaultValue = "date_desc") String sortBy) {
        try {
            List<MedicalAppointmentResponse> consultations;

            if (year != null) {
                consultations = medicalAppointmentService.getCompletedConsultationsByPatientIdAndYear(patientId, year, sortBy);
            } else {
                consultations = medicalAppointmentService.getCompletedConsultationsByPatientId(patientId, sortBy);
            }

            return ResponseEntity.ok(consultations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving consultation history: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/consultations/child/{childId}")
    public ResponseEntity<?> getConsultationHistoryByChild(@PathVariable Long childId) {
        try {
            List<MedicalAppointmentResponse> consultations =
                    medicalAppointmentService.getCompletedConsultationsByChildId(childId);

            return ResponseEntity.ok(consultations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving child consultation history: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getAllAppointments(
            @PathVariable Long patientId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "date_desc") String sortBy,
            @RequestParam(required = false) String search) {
        try {
            List<MedicalAppointmentResponse> appointments = 
                    medicalAppointmentService.getAllAppointmentsByPatient(patientId, year, status, sortBy, search);

            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving appointments: " + e.getMessage() + "\"}");
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id) {
        try {
            MedicalAppointment appointment = medicalAppointmentService.cancelAppointment(id);
            MedicalAppointmentResponse response = medicalAppointmentService.convertToResponse(appointment);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"Error cancelling appointment: " + e.getMessage() + "\"}");
        }
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<?> rescheduleAppointment(
            @PathVariable Long id,
            @RequestParam String newDate,
            @RequestParam(required = false) String newTime) {
        try {
            java.time.LocalDateTime parsedDate = java.time.LocalDateTime.parse(newDate, 
                    java.time.format.DateTimeFormatter.ISO_DATE_TIME);
            
            MedicalAppointment appointment = medicalAppointmentService.rescheduleAppointment(id, parsedDate, newTime);
            MedicalAppointmentResponse response = medicalAppointmentService.convertToResponse(appointment);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"Error rescheduling appointment: " + e.getMessage() + "\"}");
        }
    }
}

