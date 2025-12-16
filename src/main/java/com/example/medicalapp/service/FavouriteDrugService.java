package com.example.medicalapp.service;

import com.example.medicalapp.entity.FavouriteDrug;
import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.entity.Drug;
import com.example.medicalapp.models.FavouriteDrugRequest;
import com.example.medicalapp.models.FavouriteDrugResponse;
import com.example.medicalapp.repository.FavouriteDrugRepository;
import com.example.medicalapp.repository.PatientRepository;
import com.example.medicalapp.repository.DrugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavouriteDrugService {

    @Autowired
    private FavouriteDrugRepository favouriteDrugRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DrugRepository drugRepository;

    public List<FavouriteDrug> findAll() {
        return (List<FavouriteDrug>) favouriteDrugRepository.findAll();
    }

    public List<FavouriteDrug> findByPatientId(Long patientId) {
        return favouriteDrugRepository.findByPatientId(patientId);
    }

    public Optional<FavouriteDrug> findById(Long id) {
        return favouriteDrugRepository.findById(id);
    }

    public Optional<FavouriteDrug> findByIdAndPatientId(Long id, Long patientId) {
        return favouriteDrugRepository.findByIdAndPatientId(id, patientId);
    }

    public FavouriteDrug save(FavouriteDrug favouriteDrug) {
        return favouriteDrugRepository.save(favouriteDrug);
    }

    public void deleteById(Long id) {
        favouriteDrugRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return favouriteDrugRepository.existsById(id);
    }

    public FavouriteDrugResponse convertToResponse(FavouriteDrug favouriteDrug) {
        FavouriteDrugResponse response = new FavouriteDrugResponse();
        response.setId(favouriteDrug.getId());
        response.setCreatedAt(favouriteDrug.getCreatedAt());

        if (favouriteDrug.getPatient() != null) {
            response.setPatientId(favouriteDrug.getPatient().getId());
        }

        if (favouriteDrug.getDrug() != null) {
            Drug drug = favouriteDrug.getDrug();
            response.setDrugId(drug.getId());
            response.setTitle(drug.getTitle());
            response.setShortDescription(drug.getShortDescription());
            response.setDescription(drug.getDescription());
            response.setPrice(drug.getPrice());
            response.setType(drug.getType());
            response.setDosage(drug.getDosage());
            response.setImagePath(drug.getImagePath());
        }

        return response;
    }

    public List<FavouriteDrugResponse> findFavouriteDrugResponsesByPatientId(Long patientId) {
        return favouriteDrugRepository.findByPatientId(patientId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public Optional<FavouriteDrugResponse> findFavouriteDrugResponseById(Long id) {
        return favouriteDrugRepository.findById(id)
                .map(this::convertToResponse);
    }

    public FavouriteDrug createFavouriteDrug(Long patientId, FavouriteDrugRequest request) {
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }

        if (request.getDrugId() == null) {
            throw new RuntimeException("Drug ID is required");
        }

        Optional<Drug> drugOpt = drugRepository.findById(request.getDrugId());
        if (drugOpt.isEmpty()) {
            throw new RuntimeException("Drug not found with id: " + request.getDrugId());
        }

        // Проверяем, не добавлено ли уже это лекарство в избранное
        if (favouriteDrugRepository.existsByPatientIdAndDrugId(patientId, request.getDrugId())) {
            throw new RuntimeException("Drug is already in favourites");
        }

        FavouriteDrug favouriteDrug = new FavouriteDrug();
        favouriteDrug.setPatient(patientOpt.get());
        favouriteDrug.setDrug(drugOpt.get());

        return favouriteDrugRepository.save(favouriteDrug);
    }
}



