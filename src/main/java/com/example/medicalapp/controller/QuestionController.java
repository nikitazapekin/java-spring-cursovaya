package com.example.medicalapp.controller;

import com.example.medicalapp.entity.Question;
import com.example.medicalapp.models.QuestionResponse;
import com.example.medicalapp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public ResponseEntity<?> getAllQuestions() {
        try {
            List<QuestionResponse> questions = questionService.findAll().stream()
                    .map(questionService::convertToResponse)
                    .toList();
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving questions: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable Long id) {
        try {
            Optional<QuestionResponse> questionResponseOpt = questionService.findQuestionResponseById(id);

            if (questionResponseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Question not found\"}");
            }

            return ResponseEntity.ok(questionResponseOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving question: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<?> getQuestionsBySurveyId(@PathVariable Long surveyId) {
        try {
            List<QuestionResponse> questions = questionService.findQuestionResponsesBySurveyId(surveyId);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving questions for survey: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/survey/{surveyId}")
    public ResponseEntity<?> createQuestionForSurvey(
            @PathVariable Long surveyId,
            @RequestBody Question question) {
        try {
            Question savedQuestion = questionService.createQuestionForSurvey(surveyId, question);
            QuestionResponse response = questionService.convertToResponse(savedQuestion);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error creating question: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        try {
            if (!questionService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Question not found\"}");
            }

            question.setId(id);
            Question updatedQuestion = questionService.save(question);
            QuestionResponse response = questionService.convertToResponse(updatedQuestion);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error updating question: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        try {
            if (!questionService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Question not found\"}");
            }

            questionService.deleteById(id);
            return ResponseEntity.ok("{\"message\": \"Question deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error deleting question: " + e.getMessage() + "\"}");
        }
    }
}