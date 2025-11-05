package com.example.medicalapp.controller;

import com.example.medicalapp.entity.Survey;
import com.example.medicalapp.models.SurveyResponse;
import com.example.medicalapp.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @GetMapping
    public ResponseEntity<?> getAllSurveys() {
        try {
            List<SurveyResponse> surveys = surveyService.findAllSurveyResponses();
            return ResponseEntity.ok(surveys);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving surveys: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSurveyById(@PathVariable Long id) {
        try {
            Optional<SurveyResponse> surveyResponseOpt = surveyService.findSurveyResponseById(id);

            if (surveyResponseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Survey not found\"}");
            }

            return ResponseEntity.ok(surveyResponseOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving survey: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getSurveysByCategory(@PathVariable String category) {
        try {
            List<Survey> surveys = surveyService.findByCategory(category);
            List<SurveyResponse> responses = surveys.stream()
                    .map(surveyService::convertToResponse)
                    .toList();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving surveys by category: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping
    public ResponseEntity<?> createSurvey(@RequestBody Survey survey) {
        try {
            Survey savedSurvey = surveyService.save(survey);
            SurveyResponse response = surveyService.convertToResponse(savedSurvey);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error creating survey: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSurvey(@PathVariable Long id, @RequestBody Survey survey) {
        try {
            if (!surveyService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Survey not found\"}");
            }

            survey.setId(id);
            Survey updatedSurvey = surveyService.save(survey);
            SurveyResponse response = surveyService.convertToResponse(updatedSurvey);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error updating survey: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSurvey(@PathVariable Long id) {
        try {
            if (!surveyService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Survey not found\"}");
            }

            surveyService.deleteById(id);
            return ResponseEntity.ok("{\"message\": \"Survey deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error deleting survey: " + e.getMessage() + "\"}");
        }
    }
}