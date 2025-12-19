
package com.example.medicalapp.controller;

import com.example.medicalapp.models.*;
import com.example.medicalapp.service.MedicalCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-cards")
public class MedicalCardController {

    @Autowired
    private MedicalCardService medicalCardService;

    @PostMapping("/child/{childId}")
    public ResponseEntity<?> createMedicalCardForChild(@PathVariable Long childId) {
        try {
            MedicalCardResponse medicalCard = medicalCardService.createMedicalCardForChild(childId);
            return ResponseEntity.status(HttpStatus.CREATED).body(medicalCard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error creating medical card: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/child/{childId}")
    public ResponseEntity<?> getMedicalCardByChildId(@PathVariable Long childId) {
        try {
            var medicalCardOpt = medicalCardService.getMedicalCardByChildId(childId);
            if (medicalCardOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Medical card not found for this child\"}");
            }
            return ResponseEntity.ok(medicalCardOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving medical card: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/{medicalCardId}/disease-history")
    public ResponseEntity<?> addDiseaseHistory(@PathVariable Long medicalCardId,
                                               @RequestBody DiseaseHistoryRequest request) {
        try {
            DiseaseHistoryResponse diseaseHistory = medicalCardService.addDiseaseHistory(medicalCardId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(diseaseHistory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error adding disease history: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{medicalCardId}/disease-history")
    public ResponseEntity<?> getDiseaseHistories(@PathVariable Long medicalCardId) {
        try {
            List<DiseaseHistoryResponse> diseaseHistories = medicalCardService.getDiseaseHistories(medicalCardId);
            return ResponseEntity.ok(diseaseHistories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving disease histories: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/{medicalCardId}/medical-tests")
    public ResponseEntity<?> addMedicalTest(@PathVariable Long medicalCardId,
                                            @RequestBody MedicalTestRequest request) {
        try {
            MedicalTestResponse medicalTest = medicalCardService.addMedicalTest(medicalCardId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(medicalTest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error adding medical test: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{medicalCardId}/medical-tests")
    public ResponseEntity<?> getMedicalTests(@PathVariable Long medicalCardId) {
        try {
            List<MedicalTestResponse> medicalTests = medicalCardService.getMedicalTests(medicalCardId);
            return ResponseEntity.ok(medicalTests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving medical tests: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/{medicalCardId}/appointments")
    public ResponseEntity<?> addMedicalAppointment(@PathVariable Long medicalCardId,
                                                   @RequestBody MedicalAppointmentRequest request) {
        try {
            MedicalAppointmentResponse appointment = medicalCardService.addMedicalAppointment(medicalCardId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error adding medical appointment: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{medicalCardId}/appointments")
    public ResponseEntity<?> getMedicalAppointments(@PathVariable Long medicalCardId) {
        try {
            List<MedicalAppointmentResponse> appointments = medicalCardService.getMedicalAppointments(medicalCardId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving medical appointments: " + e.getMessage() + "\"}");
        }
    }








    @GetMapping("/{medicalCardId}/disease-history/by-date")
    public ResponseEntity<?> getDiseaseHistoriesByDate(
            @PathVariable Long medicalCardId,
            @RequestParam String date) {

        System.out.println("DATAAAAA");


        System.out.println(date);
        try {
            List<DiseaseHistoryResponse> diseaseHistories =
                    medicalCardService.getDiseaseHistoriesByDate(medicalCardId, date);
            return ResponseEntity.ok(diseaseHistories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving disease histories by date: " + e.getMessage() + "\"}");
        }
    }


    @GetMapping("/{medicalCardId}/medical-tests/by-date")
    public ResponseEntity<?> getMedicalTestsByDate(
            @PathVariable Long medicalCardId,
            @RequestParam String date) {

        System.out.println("DATTATA");

        System.out.println(date);
        try {
            List<MedicalTestResponse> medicalTests =
                    medicalCardService.getMedicalTestsByDate(medicalCardId, date);
            return ResponseEntity.ok(medicalTests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving medical tests by date: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{medicalCardId}/appointments/by-date")
    public ResponseEntity<?> getMedicalAppointmentsByDate(
            @PathVariable Long medicalCardId,
            @RequestParam String date) {
        try {
            List<MedicalAppointmentResponse> appointments =
                    medicalCardService.getMedicalAppointmentsByDate(medicalCardId, date);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving medical appointments by date: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{medicalCardId}/disease-history/by-period")
    public ResponseEntity<?> getDiseaseHistoriesByPeriod(
            @PathVariable Long medicalCardId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            List<DiseaseHistoryResponse> diseaseHistories =
                    medicalCardService.getDiseaseHistoriesByPeriod(medicalCardId, startDate, endDate);
            return ResponseEntity.ok(diseaseHistories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving disease histories by period: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{medicalCardId}/medical-tests/by-period")
    public ResponseEntity<?> getMedicalTestsByPeriod(
            @PathVariable Long medicalCardId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            List<MedicalTestResponse> medicalTests =
                    medicalCardService.getMedicalTestsByPeriod(medicalCardId, startDate, endDate);
            return ResponseEntity.ok(medicalTests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving medical tests by period: " + e.getMessage() + "\"}");
        }
    }



}