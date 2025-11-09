package com.example.medicalapp.service;

import com.example.medicalapp.entity.Question;
import com.example.medicalapp.entity.Survey;
import com.example.medicalapp.models.QuestionResponse;
import com.example.medicalapp.repository.QuestionRepository;
import com.example.medicalapp.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    public List<Question> findAll() {
        return (List<Question>) questionRepository.findAll();
    }

    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    public List<Question> findBySurveyId(Long surveyId) {
        return questionRepository.findBySurveyId(surveyId);
    }

    public Question save(Question question) {
        return questionRepository.save(question);
    }

    public void deleteById(Long id) {
        questionRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return questionRepository.existsById(id);
    }

    public QuestionResponse convertToResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setTitle(question.getTitle());
        response.setQuestions(question.getQuestions());
        response.setImage(question.getImage());
        response.setCreatedAt(question.getCreatedAt());

        if (question.getSurvey() != null) {
            response.setSurveyId(question.getSurvey().getId());
        }

        return response;
    }

    public Optional<QuestionResponse> findQuestionResponseById(Long id) {
        return questionRepository.findById(id)
                .map(this::convertToResponse);
    }

    public List<QuestionResponse> findQuestionResponsesBySurveyId(Long surveyId) {
        return findBySurveyId(surveyId).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public Question createQuestionForSurvey(Long surveyId, Question question) {
        Optional<Survey> surveyOpt = surveyRepository.findById(surveyId);

        if (surveyOpt.isPresent()) {
            Survey survey = surveyOpt.get();
            question.setSurvey(survey);
            return questionRepository.save(question);
        } else {
            throw new RuntimeException("Survey not found with id: " + surveyId);
        }
    }

    public boolean surveyExists(Long surveyId) {
        return surveyRepository.existsById(surveyId);
    }
}