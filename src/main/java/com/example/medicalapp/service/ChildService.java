package com.example.medicalapp.service;


import com.example.medicalapp.entity.Child;
import com.example.medicalapp.entity.ChildIdentifier;
import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.models.ChildRequest;
import com.example.medicalapp.models.ChildResponse;
import com.example.medicalapp.repository.ChildIdentifierRepository;
import com.example.medicalapp.repository.ChildRepository;
import com.example.medicalapp.repository.MedicalCardRepository;
import com.example.medicalapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ChildIdentifierRepository childIdentifierRepository;

    @Autowired
    private MedicalCardRepository medicalCardRepository;

    private static final int IDENTIFIER_LENGTH = 10;
    private static final String DIGITS = "0123456789";
    private final Random random = new Random();

    public List<ChildResponse> getChildrenByParentId(Long parentId) {
        List<Child> children = childRepository.findByParentId(parentId);
        return children.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ChildResponse> getChildrenByParentEmail(String email) {
        List<Child> children = childRepository.findByParentUserEmail(email);
        return children.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<ChildResponse> getChildById(Long id) {
        return childRepository.findById(id)
                .map(this::convertToResponse);
    }

    public Optional<ChildResponse> getChildByIdAndParentId(Long id, Long parentId) {
        return childRepository.findByIdAndParentId(id, parentId)
                .map(this::convertToResponse);
    }

    @Autowired
    private MedicalCardService medicalCardService;

    @Transactional
    public ChildResponse createChild(ChildRequest childRequest) {
        Optional<Patient> parentOpt = patientRepository.findById(childRequest.getParentId());
        if (parentOpt.isEmpty()) {
            throw new RuntimeException("Parent not found with id: " + childRequest.getParentId());
        }

        Child child = new Child();
        child.setName(childRequest.getName());
        child.setAge(childRequest.getAge());
        child.setGender(childRequest.getGender());
        child.setAvatar(childRequest.getAvatar());
        child.setParent(parentOpt.get());

        Child savedChild = childRepository.save(child);

        String identifier = generateUniqueIdentifier();
        ChildIdentifier childIdentifier = new ChildIdentifier(savedChild, identifier);
        childIdentifierRepository.save(childIdentifier);
        savedChild.setChildIdentifier(childIdentifier);

        medicalCardService.createMedicalCardForChild(savedChild.getId());

        return convertToResponse(savedChild);
    }
    public Optional<ChildResponse> updateChild(Long id, ChildRequest childRequest) {
        return childRepository.findById(id)
                .map(child -> {
                    if (childRequest.getName() != null) {
                        child.setName(childRequest.getName());
                    }
                    if (childRequest.getAge() != null) {
                        child.setAge(childRequest.getAge());
                    }
                    if (childRequest.getGender() != null) {
                        child.setGender(childRequest.getGender());
                    }
                    if (childRequest.getAvatar() != null) {
                        child.setAvatar(childRequest.getAvatar());
                    }

                    Child updatedChild = childRepository.save(child);
                    return convertToResponse(updatedChild);
                });
    }

    @Transactional
    public boolean deleteChild(Long id) {
        if (childRepository.existsById(id)) {
            // Удаляем медицинскую карту ребенка (с каскадным удалением связанных записей)
            medicalCardRepository.findByChildId(id).ifPresent(medicalCardRepository::delete);
            
            // Удаляем идентификатор ребенка
            childIdentifierRepository.findByChildId(id).ifPresent(childIdentifierRepository::delete);
            
            // Удаляем самого ребенка
            childRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteChildByParentId(Long id, Long parentId) {
        Optional<Child> childOpt = childRepository.findByIdAndParentId(id, parentId);
        if (childOpt.isPresent()) {
            // Удаляем медицинскую карту ребенка (с каскадным удалением связанных записей)
            medicalCardRepository.findByChildId(id).ifPresent(medicalCardRepository::delete);
            
            // Удаляем идентификатор ребенка
            childIdentifierRepository.findByChildId(id).ifPresent(childIdentifierRepository::delete);
            
            // Удаляем самого ребенка
            childRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public ChildResponse convertToResponse(Child child) {
        ChildResponse response = new ChildResponse();
        response.setId(child.getId());
        response.setAvatar(child.getAvatar());
        response.setName(child.getName());
        response.setAge(child.getAge());
        response.setGender(child.getGender());
        response.setParentId(child.getParent().getId());
        response.setCreatedAt(child.getCreatedAt());
        response.setUpdatedAt(child.getUpdatedAt());
        
        // Добавляем clinic информацию
        if (child.getClinic() != null) {
            response.setClinicId(child.getClinic().getId());
        }
        response.setClinicRegistrationDate(child.getClinicRegistrationDate());

        if (child.getChildIdentifier() != null) {
            response.setIdentifier(child.getChildIdentifier().getIdentifier());
        }

        return response;
    }

    private String generateUniqueIdentifier() {
        String identifier;
        do {
            identifier = generateRandomIdentifier();
        } while (childIdentifierRepository.existsByIdentifier(identifier));

        return identifier;
    }

    private String generateRandomIdentifier() {
        StringBuilder sb = new StringBuilder(IDENTIFIER_LENGTH);
        for (int i = 0; i < IDENTIFIER_LENGTH; i++) {
            sb.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return sb.toString();
    }

    public Optional<ChildResponse> getChildByIdentifier(String identifier) {
        return childIdentifierRepository.findByIdentifier(identifier)
                .map(ChildIdentifier::getChild)
                .map(this::convertToResponse);
    }



    public Optional<ChildResponse> getChildWithIdentifier(Long id) {
        return childRepository.findById(id)
                .map(child -> {
                    ChildResponse response = convertToResponse(child);

                    if (response.getIdentifier() == null) {

                        childIdentifierRepository.findByChildId(id)
                                .ifPresent(identifier -> response.setIdentifier(identifier.getIdentifier()));
                    }
                    return response;
                });
    }



}