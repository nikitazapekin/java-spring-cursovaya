package com.example.medicalapp.repository;

import com.example.medicalapp.entity.Survey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends CrudRepository<Survey, Long> {
    List<Survey> findByCategory(String category);
    List<Survey> findByTitleContainingIgnoreCase(String title);

    // Явно объявляем методы которые могут быть нужны
    Optional<Survey> findById(Long id);
    boolean existsById(Long id);
    boolean existsByTitleAndCategory(String title, String category);
}