package com.example.medicalapp.repository;

import com.example.medicalapp.entity.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findBySurveyId(Long surveyId);
    Optional<Question> findById(Long id);
    boolean existsBySurveyIdAndTitle(Long surveyId, String title);
}