
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
    List<MedicalAppointment> findByMedicalCardIdAndAppointmentType(Long medicalCardId, String appointmentType);
    List<MedicalAppointment> findByMedicalCardIdAndAppointmentDateBetween(
            Long medicalCardId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT ma FROM MedicalAppointment ma " +
           "JOIN ma.medicalCard mc " +
           "JOIN mc.child c " +
           "WHERE c.parent.id = :patientId " +
           "AND ma.status = :status " +
           "ORDER BY ma.completedAt DESC")
    List<MedicalAppointment> findByPatientIdAndStatus(@Param("patientId") Long patientId, 
                                                       @Param("status") AppointmentStatus status);

    @Query("SELECT ma FROM MedicalAppointment ma " +
           "JOIN ma.medicalCard mc " +
           "JOIN mc.child c " +
           "WHERE c.parent.id = :patientId " +
           "AND ma.status = 'COMPLETED' " +
           "AND EXTRACT(YEAR FROM ma.completedAt) = :year " +
           "ORDER BY ma.completedAt DESC")
    List<MedicalAppointment> findCompletedByPatientIdAndYear(@Param("patientId") Long patientId, 
                                                              @Param("year") Integer year);

    @Query("SELECT ma FROM MedicalAppointment ma " +
           "JOIN ma.medicalCard mc " +
           "WHERE mc.child.id = :childId " +
           "AND ma.status = 'COMPLETED' " +
           "ORDER BY ma.completedAt DESC")
    List<MedicalAppointment> findCompletedByChildId(@Param("childId") Long childId);

    @Query("SELECT ma FROM MedicalAppointment ma " +
           "JOIN ma.medicalCard mc " +
           "JOIN mc.child c " +
           "WHERE c.parent.id = :patientId " +
           "ORDER BY ma.appointmentDate DESC")
    List<MedicalAppointment> findAllByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT ma FROM MedicalAppointment ma " +
           "JOIN ma.medicalCard mc " +
           "JOIN mc.child c " +
           "WHERE c.parent.id = :patientId " +
           "AND EXTRACT(YEAR FROM ma.appointmentDate) = :year " +
           "ORDER BY ma.appointmentDate DESC")
    List<MedicalAppointment> findByPatientIdAndYear(@Param("patientId") Long patientId, 
                                                     @Param("year") Integer year);

    @Query("SELECT ma FROM MedicalAppointment ma " +
           "JOIN ma.medicalCard mc " +
           "JOIN mc.child c " +
           "WHERE c.parent.id = :patientId " +
           "AND EXTRACT(YEAR FROM ma.appointmentDate) = :year " +
           "AND ma.status = :status " +
           "ORDER BY ma.appointmentDate DESC")
    List<MedicalAppointment> findByPatientIdYearAndStatus(@Param("patientId") Long patientId, 
                                                           @Param("year") Integer year,
                                                           @Param("status") AppointmentStatus status);

    @Query(value = "SELECT * FROM medical_appointments ma " +
           "WHERE ma.doctor_id = :doctorId " +
           "AND DATE(ma.appointment_date) = CURRENT_DATE " +
           "AND ma.status = 'SCHEDULED' " +
           "ORDER BY ma.appointment_date ASC", 
           nativeQuery = true)
    List<MedicalAppointment> findTodayAppointmentsByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT ma FROM MedicalAppointment ma " +
           "WHERE ma.doctor.id = :doctorId " +
           "ORDER BY ma.appointmentDate DESC")
    List<MedicalAppointment> findAllByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT ma.appointmentTime FROM MedicalAppointment ma " +
           "WHERE ma.doctor.id = :doctorId " +
           "AND DATE(ma.appointmentDate) = DATE(:date) " +
           "AND ma.status IN ('SCHEDULED', 'PENDING')")
    List<String> findBookedTimeSlotsByDoctorAndDate(@Param("doctorId") Long doctorId, 
                                                     @Param("date") LocalDateTime date);
}