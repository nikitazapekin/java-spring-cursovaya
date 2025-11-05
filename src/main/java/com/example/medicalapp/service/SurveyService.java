package com.example.medicalapp.service;

import com.example.medicalapp.entity.Survey;
import com.example.medicalapp.models.SurveyResponse;
import com.example.medicalapp.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    // Убираем зависимость от QuestionService
    // Вместо этого используем прямые вызовы к репозиториям или DTO

    public List<Survey> findAll() {
        return (List<Survey>) surveyRepository.findAll();
    }

    public Optional<Survey> findById(Long id) {
        return surveyRepository.findById(id);
    }

    public List<Survey> findByCategory(String category) {
        return surveyRepository.findByCategory(category);
    }

    public Survey save(Survey survey) {
        return surveyRepository.save(survey);
    }

    public void deleteById(Long id) {
        surveyRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return surveyRepository.existsById(id);
    }

    public SurveyResponse convertToResponse(Survey survey) {
        SurveyResponse response = new SurveyResponse();
        response.setId(survey.getId());
        response.setTitle(survey.getTitle());
        response.setCategory(survey.getCategory());
        response.setImage(survey.getImage());
        response.setCreatedAt(survey.getCreatedAt());

        // Вопросы будут загружаться лениво или через отдельный endpoint
        // Не загружаем вопросы здесь чтобы избежать циклических зависимостей
        // response.setQuestions(null);

        return response;
    }

    public SurveyResponse convertToResponseWithQuestions(Survey survey) {
        SurveyResponse response = convertToResponse(survey);

        // Если вопросы уже загружены (например, через JOIN FETCH в репозитории)
        if (survey.getQuestions() != null && !survey.getQuestions().isEmpty()) {
            // Создаем простой список вопросов без циклических ссылок
            // response.setQuestions(...); // Раскомментировать если нужно
        }

        return response;
    }

    public Optional<SurveyResponse> findSurveyResponseById(Long id) {
        return surveyRepository.findById(id)
                .map(this::convertToResponse);
    }

    public List<SurveyResponse> findAllSurveyResponses() {
        return findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }
}