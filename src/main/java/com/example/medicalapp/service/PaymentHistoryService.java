package com.example.medicalapp.service;

import com.example.medicalapp.entity.PaymentHistory;
import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.models.PaymentHistoryRequest;
import com.example.medicalapp.models.PaymentHistoryResponse;
import com.example.medicalapp.repository.PaymentHistoryRepository;
import com.example.medicalapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentHistoryService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private PatientRepository patientRepository;

    public List<PaymentHistory> findAll() {
        return (List<PaymentHistory>) paymentHistoryRepository.findAll();
    }

    public List<PaymentHistory> findByPatientId(Long patientId) {
        return paymentHistoryRepository.findByPatientId(patientId);
    }

    public List<PaymentHistory> findAllByOrderByDateDesc() {
        return paymentHistoryRepository.findAllByOrderByDateDesc();
    }

    public Optional<PaymentHistory> findById(Long id) {
        return paymentHistoryRepository.findById(id);
    }

    public Optional<PaymentHistory> findByIdAndPatientId(Long id, Long patientId) {
        return paymentHistoryRepository.findByIdAndPatientId(id, patientId);
    }

    public PaymentHistory save(PaymentHistory paymentHistory) {
        return paymentHistoryRepository.save(paymentHistory);
    }

    public void deleteById(Long id) {
        paymentHistoryRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return paymentHistoryRepository.existsById(id);
    }

    public PaymentHistoryResponse convertToResponse(PaymentHistory paymentHistory) {
        PaymentHistoryResponse response = new PaymentHistoryResponse();
        response.setId(paymentHistory.getId());
        response.setDate(paymentHistory.getDate());
        response.setTitle(paymentHistory.getTitle());
        response.setDescription(paymentHistory.getDescription());
        response.setPrice(paymentHistory.getPrice());
        response.setDoctor(paymentHistory.getDoctor());
        response.setCreatedAt(paymentHistory.getCreatedAt());

        if (paymentHistory.getPatient() != null) {
            response.setPatientId(paymentHistory.getPatient().getId());
            response.setPatientName(paymentHistory.getPatient().getFirstName() + " " + paymentHistory.getPatient().getLastName());
        }

        return response;
    }

    public Optional<PaymentHistoryResponse> findPaymentResponseById(Long id) {
        return paymentHistoryRepository.findById(id)
                .map(this::convertToResponse);
    }

    public List<PaymentHistoryResponse> findPaymentResponsesByPatientId(Long patientId) {
        return paymentHistoryRepository.findByPatientId(patientId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public PaymentHistory createPayment(PaymentHistoryRequest request) {
        Optional<Patient> patientOpt = patientRepository.findById(request.getPatientId());
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found with id: " + request.getPatientId());
        }

        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setDate(request.getDate());
        paymentHistory.setTitle(request.getTitle());
        paymentHistory.setDescription(request.getDescription());
        paymentHistory.setPrice(request.getPrice());
        paymentHistory.setDoctor(request.getDoctor());
        paymentHistory.setPatient(patientOpt.get());

        return paymentHistoryRepository.save(paymentHistory);
    }

    public PaymentHistory updatePayment(Long id, PaymentHistoryRequest request) {
        Optional<PaymentHistory> paymentOpt = paymentHistoryRepository.findById(id);
        if (paymentOpt.isEmpty()) {
            throw new RuntimeException("Payment history not found with id: " + id);
        }

        PaymentHistory paymentHistory = paymentOpt.get();

        if (request.getDate() != null) {
            paymentHistory.setDate(request.getDate());
        }
        if (request.getTitle() != null) {
            paymentHistory.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            paymentHistory.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            paymentHistory.setPrice(request.getPrice());
        }
        if (request.getDoctor() != null) {
            paymentHistory.setDoctor(request.getDoctor());
        }
        if (request.getPatientId() != null && !request.getPatientId().equals(paymentHistory.getPatient().getId())) {
            Optional<Patient> patientOpt = patientRepository.findById(request.getPatientId());
            if (patientOpt.isEmpty()) {
                throw new RuntimeException("Patient not found with id: " + request.getPatientId());
            }
            paymentHistory.setPatient(patientOpt.get());
        }

        return paymentHistoryRepository.save(paymentHistory);
    }
}