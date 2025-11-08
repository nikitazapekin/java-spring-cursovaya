package com.example.medicalapp.repository;

import com.example.medicalapp.entity.DoctorChats;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorChatsRepository extends CrudRepository<DoctorChats, Long> {
    List<DoctorChats> findByDoctorId(Long doctorId);
    Optional<DoctorChats> findByDoctorIdAndPatientId(Long doctorId, Long patientId);
}