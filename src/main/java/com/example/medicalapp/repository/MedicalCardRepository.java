
package com.example.medicalapp.repository;

import com.example.medicalapp.entity.MedicalCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalCardRepository extends CrudRepository<MedicalCard, Long> {
    Optional<MedicalCard> findByChildId(Long childId);
    boolean existsByChildId(Long childId);
}