package com.example.medicalapp.service;

import com.example.medicalapp.entity.Drug;
import com.example.medicalapp.models.DrugRequest;
import com.example.medicalapp.models.DrugResponse;
import com.example.medicalapp.repository.DrugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DrugService {

    @Autowired
    private DrugRepository drugRepository;

    public List<Drug> findAll() {
        return drugRepository.findAll();
    }

    public Optional<Drug> findById(Long id) {
        return drugRepository.findById(id);
    }

    public Drug save(Drug drug) {
        return drugRepository.save(drug);
    }

    public void deleteById(Long id) {
        drugRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return drugRepository.existsById(id);
    }

    public DrugResponse convertToResponse(Drug drug) {
        DrugResponse response = new DrugResponse();
        response.setId(drug.getId());
        response.setTitle(drug.getTitle());
        response.setShortDescription(drug.getShortDescription());
        response.setDescription(drug.getDescription());
        response.setPrice(drug.getPrice());
        response.setType(drug.getType());
        response.setDosage(drug.getDosage());
        response.setImagePath(drug.getImagePath());
        response.setCreatedAt(drug.getCreatedAt());
        return response;
    }

    public List<DrugResponse> findAllDrugResponses() {
        return drugRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<DrugResponse> findDrugsWithSearchAndSort(String searchQuery, String sortBy) {
        List<Drug> drugs;
        
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            drugs = drugRepository.findAll();
        } else {
            String query = searchQuery.trim();
            drugs = drugRepository.findDrugsWithSearch(query);
        }

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            drugs = sortDrugs(drugs, sortBy.trim());
        } else {

            drugs = drugs.stream()
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .collect(Collectors.toList());
        }
        
        return drugs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private List<Drug> sortDrugs(List<Drug> drugs, String sortBy) {
        return switch (sortBy) {
            case "name" -> drugs.stream()
                    .sorted((a, b) -> {
                        String titleA = a.getTitle() != null ? a.getTitle() : "";
                        String titleB = b.getTitle() != null ? b.getTitle() : "";
                        return titleA.compareToIgnoreCase(titleB);
                    })
                    .collect(Collectors.toList());
            case "cost" -> drugs.stream()
                    .sorted((a, b) -> {
                        Double priceA = a.getPrice() != null ? a.getPrice() : 0.0;
                        Double priceB = b.getPrice() != null ? b.getPrice() : 0.0;
                        return priceA.compareTo(priceB);
                    })
                    .collect(Collectors.toList());
            case "type" -> drugs.stream()
                    .sorted((a, b) -> {
                        String typeA = a.getType() != null ? a.getType() : "";
                        String typeB = b.getType() != null ? b.getType() : "";
                        return typeA.compareToIgnoreCase(typeB);
                    })
                    .collect(Collectors.toList());
            default -> drugs;
        };
    }

    public Drug createDrug(DrugRequest request) {
        Drug drug = new Drug();
        drug.setTitle(request.getTitle());
        drug.setShortDescription(request.getShortDescription());
        drug.setDescription(request.getDescription());
        drug.setPrice(request.getPrice());
        drug.setType(request.getType());
        drug.setDosage(request.getDosage());
        drug.setImagePath(request.getImagePath());
        return drugRepository.save(drug);
    }

    public Drug updateDrug(Long id, DrugRequest request) {
        Optional<Drug> drugOpt = drugRepository.findById(id);
        if (drugOpt.isEmpty()) {
            throw new RuntimeException("Drug not found with id: " + id);
        }

        Drug drug = drugOpt.get();
        if (request.getTitle() != null) {
            drug.setTitle(request.getTitle());
        }
        if (request.getShortDescription() != null) {
            drug.setShortDescription(request.getShortDescription());
        }
        if (request.getDescription() != null) {
            drug.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            drug.setPrice(request.getPrice());
        }
        if (request.getType() != null) {
            drug.setType(request.getType());
        }
        if (request.getDosage() != null) {
            drug.setDosage(request.getDosage());
        }
        if (request.getImagePath() != null) {
            drug.setImagePath(request.getImagePath());
        }

        return drugRepository.save(drug);
    }
}

