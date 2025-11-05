
package com.example.medicalapp.repository;

import com.example.medicalapp.entity.MedicalAppointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalAppointmentRepository extends CrudRepository<MedicalAppointment, Long> {
    List<MedicalAppointment> findByMedicalCardId(Long medicalCardId);
    List<MedicalAppointment> findByMedicalCardIdAndAppointmentDateBetween(
            Long medicalCardId, LocalDateTime startDate, LocalDateTime endDate);
}