package com.example.medicalapp.controller;

import com.example.medicalapp.entity.Recommendation;
import com.example.medicalapp.models.RecommendationRequest;
import com.example.medicalapp.models.RecommendationResponse;
import com.example.medicalapp.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<?> getAllRecommendations() {
        try {
            List<RecommendationResponse> recommendations = recommendationService.findAllRecommendationResponses();
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving recommendations: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/top3")
    public ResponseEntity<?> getTop3Recommendations() {
        try {
            List<RecommendationResponse> recommendations = recommendationService.findTop3Responses();
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving top 3 recommendations: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecommendationById(@PathVariable Long id) {
        try {
            Optional<RecommendationResponse> recommendationOpt = recommendationService.findRecommendationResponseById(id);

            if (recommendationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Recommendation not found\"}");
            }

            return ResponseEntity.ok(recommendationOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving recommendation: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getRecommendationsByCategory(@PathVariable String category) {
        try {
            List<RecommendationResponse> recommendations = recommendationService.findRecommendationResponsesByCategory(category);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving recommendations by category: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping
    public ResponseEntity<?> createRecommendation(@RequestBody RecommendationRequest request) {
        try {
            Recommendation recommendation = new Recommendation(
                request.getCategory(),
                request.getTitle(),
                request.getDate(),
                request.getFullDescription(),
                request.getImagePath()
            );

            Recommendation savedRecommendation = recommendationService.save(recommendation);
            RecommendationResponse response = recommendationService.convertToResponse(savedRecommendation);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error creating recommendation: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecommendation(@PathVariable Long id, @RequestBody RecommendationRequest request) {
        try {
            if (!recommendationService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Recommendation not found\"}");
            }

            Recommendation recommendation = new Recommendation(
                request.getCategory(),
                request.getTitle(),
                request.getDate(),
                request.getFullDescription(),
                request.getImagePath()
            );
            recommendation.setId(id);

            Recommendation updatedRecommendation = recommendationService.save(recommendation);
            RecommendationResponse response = recommendationService.convertToResponse(updatedRecommendation);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error updating recommendation: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecommendation(@PathVariable Long id) {
        try {
            if (!recommendationService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Recommendation not found\"}");
            }

            recommendationService.deleteById(id);
            return ResponseEntity.ok("{\"message\": \"Recommendation deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error deleting recommendation: " + e.getMessage() + "\"}");
        }
    }
}

