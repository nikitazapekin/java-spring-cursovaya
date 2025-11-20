package com.example.medicalapp.service;

import com.example.medicalapp.entity.Recommendation;
import com.example.medicalapp.models.RecommendationResponse;
import com.example.medicalapp.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    public List<Recommendation> findAll() {
        return (List<Recommendation>) recommendationRepository.findAll();
    }

    public Optional<Recommendation> findById(Long id) {
        return recommendationRepository.findById(id);
    }

    public List<Recommendation> findByCategory(String category) {
        return recommendationRepository.findByCategory(category);
    }

    public List<Recommendation> findTop3() {
        return recommendationRepository.findTop3ByOrderByCreatedAtDesc();
    }

    public Recommendation save(Recommendation recommendation) {
        return recommendationRepository.save(recommendation);
    }

    public void deleteById(Long id) {
        recommendationRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return recommendationRepository.existsById(id);
    }

    public RecommendationResponse convertToResponse(Recommendation recommendation) {
        RecommendationResponse response = new RecommendationResponse();
        response.setId(recommendation.getId());
        response.setCategory(recommendation.getCategory());
        response.setTitle(recommendation.getTitle());
        response.setDate(recommendation.getDate());
        response.setFullDescription(recommendation.getFullDescription());
        response.setImagePath(recommendation.getImagePath());
        response.setCreatedAt(recommendation.getCreatedAt());

        return response;
    }

    public Optional<RecommendationResponse> findRecommendationResponseById(Long id) {
        return recommendationRepository.findById(id)
                .map(this::convertToResponse);
    }

    public List<RecommendationResponse> findAllRecommendationResponses() {
        return findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<RecommendationResponse> findRecommendationResponsesByCategory(String category) {
        return findByCategory(category).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<RecommendationResponse> findTop3Responses() {
        return findTop3().stream()
                .map(this::convertToResponse)
                .toList();
    }
}

