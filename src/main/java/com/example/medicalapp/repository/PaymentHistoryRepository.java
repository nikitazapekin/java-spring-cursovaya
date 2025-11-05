package com.example.medicalapp.repository;

import com.example.medicalapp.entity.PaymentHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentHistoryRepository extends CrudRepository<PaymentHistory, Long> {
    List<PaymentHistory> findByPatientId(Long patientId);
    List<PaymentHistory> findAllByOrderByDateDesc();
    Optional<PaymentHistory> findByIdAndPatientId(Long id, Long patientId);
    boolean existsByPatientId(Long patientId);
}