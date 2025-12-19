
package com.example.medicalapp.repository;

import com.example.medicalapp.entity.DiseaseHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiseaseHistoryRepository extends CrudRepository<DiseaseHistory, Long> {
    List<DiseaseHistory> findByMedicalCardId(Long medicalCardId);
    List<DiseaseHistory> findByMedicalCardIdAndStartDate(Long medicalCardId, LocalDate date);
    List<DiseaseHistory> findByMedicalCardIdAndEndDate(Long medicalCardId, LocalDate date);


    List<DiseaseHistory> findByMedicalCardIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long medicalCardId, LocalDate date1, LocalDate date);
}