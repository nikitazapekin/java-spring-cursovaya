package com.example.medicalapp.service;

import com.example.medicalapp.entity.Advice;
import com.example.medicalapp.models.AdviceResponse;
import com.example.medicalapp.repository.AdviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.List;
import java.util.Optional;

@Service
public class AdviceService {


    private final Random random = new Random();

    @Autowired
    private AdviceRepository adviceRepository;

    public List<Advice> findAll() {
        return (List<Advice>) adviceRepository.findAll();
    }

    public Optional<Advice> findById(Long id) {
        return adviceRepository.findById(id);
    }

    public List<Advice> findByType(String type) {
        return adviceRepository.findByType(type);
    }

    public Advice save(Advice advice) {
        return adviceRepository.save(advice);
    }

    public void deleteById(Long id) {
        adviceRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return adviceRepository.existsById(id);
    }

    public AdviceResponse convertToResponse(Advice advice) {
        AdviceResponse response = new AdviceResponse();
        response.setId(advice.getId());
        response.setType(advice.getType());
        response.setItems(advice.getItems());
        response.setRecommendations(advice.getRecommendations());
        response.setCreatedAt(advice.getCreatedAt());

        return response;
    }

    public Optional<AdviceResponse> findAdviceResponseById(Long id) {
        return adviceRepository.findById(id)
                .map(this::convertToResponse);
    }

    public List<AdviceResponse> findAllAdviceResponses() {
        return findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<AdviceResponse> findAdviceResponsesByType(String type) {
        return findByType(type).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public Optional<AdviceResponse> findRandomAdvice() {
        List<Advice> allAdvice = findAll();

        if (allAdvice.isEmpty()) {
            return Optional.empty();
        }

        Advice randomAdvice = allAdvice.get(random.nextInt(allAdvice.size()));
        return Optional.of(convertToResponse(randomAdvice));
    }

    public Optional<AdviceResponse> findRandomAdviceByType(String type) {
        List<Advice> adviceByType = findByType(type);

        if (adviceByType.isEmpty()) {
            return Optional.empty();
        }

        Advice randomAdvice = adviceByType.get(random.nextInt(adviceByType.size()));
        return Optional.of(convertToResponse(randomAdvice));
    }


}