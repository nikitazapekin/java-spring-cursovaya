package com.example.medicalapp.service;

import com.example.medicalapp.entity.FavouriteDrug;
import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.models.FavouriteDrugRequest;
import com.example.medicalapp.models.FavouriteDrugResponse;
import com.example.medicalapp.repository.FavouriteDrugRepository;
import com.example.medicalapp.repository.PatientRepository;
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
        response.setTitle(favouriteDrug.getTitle());
        response.setDescription(favouriteDrug.getDescription());
        response.setPrice(favouriteDrug.getPrice());
        response.setType(favouriteDrug.getType());
        response.setDosage(favouriteDrug.getDosage());
        response.setCreatedAt(favouriteDrug.getCreatedAt());

        if (favouriteDrug.getPatient() != null) {
            response.setPatientId(favouriteDrug.getPatient().getId());
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

        FavouriteDrug favouriteDrug = new FavouriteDrug();
        favouriteDrug.setPatient(patientOpt.get());
        favouriteDrug.setTitle(request.getTitle());
        favouriteDrug.setDescription(request.getDescription());
        favouriteDrug.setPrice(request.getPrice());
        favouriteDrug.setType(request.getType());
        favouriteDrug.setDosage(request.getDosage());

        return favouriteDrugRepository.save(favouriteDrug);
    }

    public FavouriteDrug updateFavouriteDrug(Long id, FavouriteDrugRequest request) {
        Optional<FavouriteDrug> drugOpt = favouriteDrugRepository.findById(id);
        if (drugOpt.isEmpty()) {
            throw new RuntimeException("Favourite drug not found with id: " + id);
        }

        FavouriteDrug favouriteDrug = drugOpt.get();

        if (request.getTitle() != null) {
            favouriteDrug.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            favouriteDrug.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            favouriteDrug.setPrice(request.getPrice());
        }
        if (request.getType() != null) {
            favouriteDrug.setType(request.getType());
        }
        if (request.getDosage() != null) {
            favouriteDrug.setDosage(request.getDosage());
        }

        return favouriteDrugRepository.save(favouriteDrug);
    }
}


