package com.example.medicalapp.repository;

import com.example.medicalapp.entity.Chat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Long> {
    Optional<Chat> findByPatientIdAndDoctorId(Long patientId, Long doctorId);
    List<Chat> findByPatientId(Long patientId);
    List<Chat> findByDoctorId(Long doctorId);
}