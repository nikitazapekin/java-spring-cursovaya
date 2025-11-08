package com.example.medicalapp.repository;

import com.example.medicalapp.entity.ChatBase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatBaseRepository extends CrudRepository<ChatBase, Long> {
    Optional<ChatBase> findByPatientIdAndDoctorId(Long patientId, Long doctorId);
}