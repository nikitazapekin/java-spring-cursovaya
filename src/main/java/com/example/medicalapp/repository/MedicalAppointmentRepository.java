
package com.example.medicalapp.repository;

import com.example.medicalapp.entity.AppointmentStatus;
import com.example.medicalapp.entity.MedicalAppointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalAppointmentRepository extends CrudRepository<MedicalAppointment, Long> {
    List<MedicalAppointment> findByMedicalCardId(Long medicalCardId);
    List<MedicalAppointment> findByMedicalCardIdAndAppointmentDateBetween(
            Long medicalCardId, LocalDateTime startDate, LocalDateTime endDate);

    // История консультаций по patient_id (через child -> medical_card)
    @Query("SELECT ma FROM MedicalAppointment ma " +
           "JOIN ma.medicalCard mc " +
           "JOIN mc.child c " +
           "WHERE c.parent.id = :patientId " +
           "AND ma.status = :status " +
           "ORDER BY ma.completedAt DESC")
    List<MedicalAppointment> findByPatientIdAndStatus(@Param("patientId") Long patientId, 
                                                       @Param("status") AppointmentStatus status);

    // История по году
    @Query("SELECT ma FROM MedicalAppointment ma " +
           "JOIN ma.medicalCard mc " +
           "JOIN mc.child c " +
           "WHERE c.parent.id = :patientId " +
           "AND ma.status = 'COMPLETED' " +
           "AND EXTRACT(YEAR FROM ma.completedAt) = :year " +
           "ORDER BY ma.completedAt DESC")
    List<MedicalAppointment> findCompletedByPatientIdAndYear(@Param("patientId") Long patientId, 
                                                              @Param("year") Integer year);

    // По конкретному ребенку
    @Query("SELECT ma FROM MedicalAppointment ma " +
           "JOIN ma.medicalCard mc " +
           "WHERE mc.child.id = :childId " +
           "AND ma.status = 'COMPLETED' " +
           "ORDER BY ma.completedAt DESC")
    List<MedicalAppointment> findCompletedByChildId(@Param("childId") Long childId);
}