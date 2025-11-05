package com.example.medicalapp.controller;

import com.example.medicalapp.entity.PaymentHistory;
import com.example.medicalapp.models.PaymentHistoryRequest;
import com.example.medicalapp.models.PaymentHistoryResponse;
import com.example.medicalapp.service.PaymentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentHistoryController {

    @Autowired
    private PaymentHistoryService paymentHistoryService;

    @GetMapping
    public ResponseEntity<?> getAllPayments() {
        try {
            List<PaymentHistoryResponse> payments = paymentHistoryService.findAll()
                    .stream()
                    .map(paymentHistoryService::convertToResponse)
                    .toList();
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving payments: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        try {
            Optional<PaymentHistoryResponse> paymentResponseOpt = paymentHistoryService.findPaymentResponseById(id);

            if (paymentResponseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Payment not found\"}");
            }

            return ResponseEntity.ok(paymentResponseOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving payment: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPaymentsByPatientId(@PathVariable Long patientId) {
        try {
            List<PaymentHistoryResponse> payments = paymentHistoryService.findPaymentResponsesByPatientId(patientId);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving patient payments: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentHistoryRequest request) {
        try {
            PaymentHistory payment = paymentHistoryService.createPayment(request);
            PaymentHistoryResponse response = paymentHistoryService.convertToResponse(payment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error creating payment: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable Long id, @RequestBody PaymentHistoryRequest request) {
        try {
            PaymentHistory payment = paymentHistoryService.updatePayment(id, request);
            PaymentHistoryResponse response = paymentHistoryService.convertToResponse(payment);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"Payment not found\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error updating payment: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
        try {
            if (!paymentHistoryService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Payment not found\"}");
            }

            paymentHistoryService.deleteById(id);
            return ResponseEntity.ok()
                    .body("{\"message\": \"Payment deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error deleting payment: " + e.getMessage() + "\"}");
        }
    }
}