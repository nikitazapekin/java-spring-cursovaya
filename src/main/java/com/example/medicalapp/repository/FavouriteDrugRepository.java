package com.example.medicalapp.repository;

import com.example.medicalapp.entity.FavouriteDrug;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteDrugRepository extends CrudRepository<FavouriteDrug, Long> {
    List<FavouriteDrug> findByPatientId(Long patientId);
    Optional<FavouriteDrug> findByIdAndPatientId(Long id, Long patientId);
    boolean existsByPatientId(Long patientId);
    Optional<FavouriteDrug> findByPatientIdAndDrugId(Long patientId, Long drugId);
    boolean existsByPatientIdAndDrugId(Long patientId, Long drugId);
}



