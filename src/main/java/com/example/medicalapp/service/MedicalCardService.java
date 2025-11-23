package com.example.medicalapp.service;

import com.example.medicalapp.entity.*;
import com.example.medicalapp.models.*;
import com.example.medicalapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicalCardService {

    @Autowired
    private MedicalCardRepository medicalCardRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private DiseaseHistoryRepository diseaseHistoryRepository;

    @Autowired
    private MedicalTestRepository medicalTestRepository;

    @Autowired
    private MedicalAppointmentRepository medicalAppointmentRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Transactional
    public MedicalCardResponse createMedicalCardForChild(Long childId) {
        Optional<Child> childOpt = childRepository.findById(childId);
        if (childOpt.isEmpty()) {
            throw new RuntimeException("Child not found with id: " + childId);
        }


        if (medicalCardRepository.existsByChildId(childId)) {
            throw new RuntimeException("Medical card already exists for child with id: " + childId);
        }

        Child child = childOpt.get();
        MedicalCard medicalCard = new MedicalCard(child);
        MedicalCard savedMedicalCard = medicalCardRepository.save(medicalCard);

        return convertToResponse(savedMedicalCard);
    }

    public Optional<MedicalCardResponse> getMedicalCardByChildId(Long childId) {
        return medicalCardRepository.findByChildId(childId)
                .map(this::convertToResponse);
    }

    public Optional<MedicalCardResponse> getMedicalCardById(Long id) {
        return medicalCardRepository.findById(id)
                .map(this::convertToResponse);
    }

    @Transactional
    public DiseaseHistoryResponse addDiseaseHistory(Long medicalCardId, DiseaseHistoryRequest request) {
        Optional<MedicalCard> medicalCardOpt = medicalCardRepository.findById(medicalCardId);
        if (medicalCardOpt.isEmpty()) {
            throw new RuntimeException("Medical card not found with id: " + medicalCardId);
        }

        MedicalCard medicalCard = medicalCardOpt.get();
        DiseaseHistory diseaseHistory = new DiseaseHistory(
                medicalCard,
                request.getDiseaseName(),
                request.getStartDate(),
                request.getEndDate(),
                request.getDescription()
        );

        DiseaseHistory savedDiseaseHistory = diseaseHistoryRepository.save(diseaseHistory);
        return convertToDiseaseHistoryResponse(savedDiseaseHistory);
    }

    @Transactional
    public MedicalTestResponse addMedicalTest(Long medicalCardId, MedicalTestRequest request) {
        Optional<MedicalCard> medicalCardOpt = medicalCardRepository.findById(medicalCardId);
        if (medicalCardOpt.isEmpty()) {
            throw new RuntimeException("Medical card not found with id: " + medicalCardId);
        }

        MedicalCard medicalCard = medicalCardOpt.get();
        MedicalTest medicalTest = new MedicalTest(
                medicalCard,
                request.getTestName(),
                request.getTestDate(),
                request.getDescription(),
                request.getDoctorName()
        );

        MedicalTest savedMedicalTest = medicalTestRepository.save(medicalTest);
        return convertToMedicalTestResponse(savedMedicalTest);
    }

    @Transactional
    public MedicalAppointmentResponse addMedicalAppointment(Long medicalCardId, MedicalAppointmentRequest request) {
        Optional<MedicalCard> medicalCardOpt = medicalCardRepository.findById(medicalCardId);
        if (medicalCardOpt.isEmpty()) {
            throw new RuntimeException("Medical card not found with id: " + medicalCardId);
        }

        MedicalCard medicalCard = medicalCardOpt.get();
        
        LocalDateTime appointmentDate = null;
        if (request.getAppointmentDate() != null) {
            appointmentDate = LocalDateTime.parse(request.getAppointmentDate(), 
                    java.time.format.DateTimeFormatter.ISO_DATE_TIME);
        }
        
        MedicalAppointment medicalAppointment = new MedicalAppointment(
                medicalCard,
                request.getAppointmentName(),
                appointmentDate,
                request.getDescription(),
                request.getAppointmentType(),
                ""
        );

        MedicalAppointment savedMedicalAppointment = medicalAppointmentRepository.save(medicalAppointment);
        return convertToMedicalAppointmentResponse(savedMedicalAppointment);
    }

    public List<DiseaseHistoryResponse> getDiseaseHistories(Long medicalCardId) {
        return diseaseHistoryRepository.findByMedicalCardId(medicalCardId)
                .stream()
                .map(this::convertToDiseaseHistoryResponse)
                .collect(Collectors.toList());
    }

    public List<MedicalTestResponse> getMedicalTests(Long medicalCardId) {
        return medicalTestRepository.findByMedicalCardId(medicalCardId)
                .stream()
                .map(this::convertToMedicalTestResponse)
                .collect(Collectors.toList());
    }

    public List<MedicalAppointmentResponse> getMedicalAppointments(Long medicalCardId) {
        return medicalAppointmentRepository.findByMedicalCardId(medicalCardId)
                .stream()
                .map(this::convertToMedicalAppointmentResponse)
                .collect(Collectors.toList());
    }


    public List<DiseaseHistoryResponse> getDiseaseHistoriesByDate(Long medicalCardId, String dateString) {
        LocalDate date = parseDate(dateString);


        List<DiseaseHistory> diseaseHistories = diseaseHistoryRepository
                .findByMedicalCardIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        medicalCardId, date, date);

        return diseaseHistories.stream()
                .map(this::convertToDiseaseHistoryResponse)
                .collect(Collectors.toList());
    }

    public List<MedicalTestResponse> getMedicalTestsByDate(Long medicalCardId, String dateString) {
        LocalDate date = parseDate(dateString);

        List<MedicalTest> medicalTests = medicalTestRepository
                .findByMedicalCardIdAndTestDate(medicalCardId, date);

        return medicalTests.stream()
                .map(this::convertToMedicalTestResponse)
                .collect(Collectors.toList());
    }

    public List<MedicalAppointmentResponse> getMedicalAppointmentsByDate(Long medicalCardId, String dateString) {
        LocalDate date = parseDate(dateString);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<MedicalAppointment> medicalAppointments = medicalAppointmentRepository
                .findByMedicalCardIdAndAppointmentDateBetween(medicalCardId, startOfDay, endOfDay);

        return medicalAppointments.stream()
                .map(this::convertToMedicalAppointmentResponse)
                .collect(Collectors.toList());
    }

    public List<DiseaseHistoryResponse> getDiseaseHistoriesByPeriod(Long medicalCardId, String startDateString, String endDateString) {
        LocalDate startDate = parseDate(startDateString);
        LocalDate endDate = parseDate(endDateString);

        List<DiseaseHistory> diseaseHistories = diseaseHistoryRepository
                .findByMedicalCardIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        medicalCardId, endDate, startDate);

        return diseaseHistories.stream()
                .map(this::convertToDiseaseHistoryResponse)
                .collect(Collectors.toList());
    }

    public List<MedicalTestResponse> getMedicalTestsByPeriod(Long medicalCardId, String startDateString, String endDateString) {
        LocalDate startDate = parseDate(startDateString);
        LocalDate endDate = parseDate(endDateString);

        List<MedicalTest> medicalTests = medicalTestRepository
                .findByMedicalCardIdAndTestDateBetween(medicalCardId, startDate, endDate);

        return medicalTests.stream()
                .map(this::convertToMedicalTestResponse)
                .collect(Collectors.toList());
    }

    private LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, dateFormatter);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format. Expected format: dd.MM.yyyy");
        }
    }

    private MedicalCardResponse convertToResponse(MedicalCard medicalCard) {
        MedicalCardResponse response = new MedicalCardResponse();
        response.setId(medicalCard.getId());
        response.setChildId(medicalCard.getChild().getId());
        response.setChildName(medicalCard.getChild().getName());
        response.setCreatedAt(medicalCard.getCreatedAt());
        response.setUpdatedAt(medicalCard.getUpdatedAt());

        response.setDiseaseHistories(
                medicalCard.getDiseaseHistories().stream()
                        .map(this::convertToDiseaseHistoryResponse)
                        .collect(Collectors.toList())
        );

        response.setMedicalTests(
                medicalCard.getMedicalTests().stream()
                        .map(this::convertToMedicalTestResponse)
                        .collect(Collectors.toList())
        );

        response.setMedicalAppointments(
                medicalCard.getMedicalAppointments().stream()
                        .map(this::convertToMedicalAppointmentResponse)
                        .collect(Collectors.toList())
        );

        return response;
    }

    private DiseaseHistoryResponse convertToDiseaseHistoryResponse(DiseaseHistory diseaseHistory) {
        DiseaseHistoryResponse response = new DiseaseHistoryResponse();
        response.setId(diseaseHistory.getId());
        response.setDiseaseName(diseaseHistory.getDiseaseName());
        response.setStartDate(diseaseHistory.getStartDate());
        response.setEndDate(diseaseHistory.getEndDate());
        response.setDescription(diseaseHistory.getDescription());
        return response;
    }

    private MedicalTestResponse convertToMedicalTestResponse(MedicalTest medicalTest) {
        MedicalTestResponse response = new MedicalTestResponse();
        response.setId(medicalTest.getId());
        response.setTestName(medicalTest.getTestName());
        response.setTestDate(medicalTest.getTestDate());
        response.setDescription(medicalTest.getDescription());
        response.setDoctorName(medicalTest.getDoctorName());
        return response;
    }

    private MedicalAppointmentResponse convertToMedicalAppointmentResponse(MedicalAppointment medicalAppointment) {
        MedicalAppointmentResponse response = new MedicalAppointmentResponse();
        response.setId(medicalAppointment.getId());
        response.setAppointmentName(medicalAppointment.getAppointmentName());
        response.setAppointmentDate(medicalAppointment.getAppointmentDate());
        response.setDescription(medicalAppointment.getDescription());
        response.setAppointmentType(medicalAppointment.getAppointmentType());
        response.setDoctorInitials(medicalAppointment.getDoctorInitials());
        return response;
    }
}