package com.example.medicalapp.repository;

import com.example.medicalapp.entity.Recommendation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends CrudRepository<Recommendation, Long> {
    List<Recommendation> findByCategory(String category);
    Optional<Recommendation> findById(Long id);
    List<Recommendation> findTop3ByOrderByCreatedAtDesc();
}

