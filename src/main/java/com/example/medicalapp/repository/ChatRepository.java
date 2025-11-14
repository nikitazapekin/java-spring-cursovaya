package com.example.medicalapp.repository;

import com.example.medicalapp.entity.Chat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE c.patient.id = :patientId AND c.doctor.id = :doctorId")
    Optional<Chat> findByPatientIdAndDoctorId(@Param("patientId") Long patientId, @Param("doctorId") Long doctorId);

    @Query("SELECT c FROM Chat c WHERE c.patient.id = :patientId")
    List<Chat> findByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT c FROM Chat c WHERE c.doctor.id = :doctorId")
    List<Chat> findByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT c FROM Chat c JOIN FETCH c.patient p JOIN FETCH c.doctor d WHERE c.id = :chatId")
    Optional<Chat> findByIdWithParticipants(@Param("chatId") Long chatId);

    @Query("SELECT c FROM Chat c JOIN FETCH c.patient p JOIN FETCH c.doctor d WHERE c.patient.id = :patientId")
    List<Chat> findByPatientIdWithParticipants(@Param("patientId") Long patientId);

    @Query("SELECT c FROM Chat c JOIN FETCH c.patient p JOIN FETCH c.doctor d WHERE c.doctor.id = :doctorId")
    List<Chat> findByDoctorIdWithParticipants(@Param("doctorId") Long doctorId);

    @Query("SELECT c FROM Chat c JOIN FETCH c.patient p JOIN FETCH c.doctor d WHERE p.id = :patientId AND d.id = :doctorId")
    Optional<Chat> findByPatientAndDoctorWithParticipants(@Param("patientId") Long patientId, @Param("doctorId") Long doctorId);
}