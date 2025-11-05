
package com.example.medicalapp.repository;

import com.example.medicalapp.entity.MedicalTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalTestRepository extends CrudRepository<MedicalTest, Long> {
    List<MedicalTest> findByMedicalCardId(Long medicalCardId);
    List<MedicalTest> findByMedicalCardIdAndTestDate(Long medicalCardId, LocalDate date);
    List<MedicalTest> findByMedicalCardIdAndTestDateBetween(Long medicalCardId, LocalDate startDate, LocalDate endDate);
}