package com.example.medicalapp.repository;


import com.example.medicalapp.entity.UserChats;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChatsRepository extends CrudRepository<UserChats, Long> {
    List<UserChats> findByPatientId(Long patientId);
    Optional<UserChats> findByPatientIdAndDoctorId(Long patientId, Long doctorId);
}