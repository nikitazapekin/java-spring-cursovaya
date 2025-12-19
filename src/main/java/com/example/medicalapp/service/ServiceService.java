package com.example.medicalapp.service;

import com.example.medicalapp.entity.Service;
import com.example.medicalapp.models.ServiceResponse;
import com.example.medicalapp.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    public Optional<Service> findById(Long id) {
        return serviceRepository.findById(id);
    }

    public Optional<Service> findByTitle(String title) {
        return serviceRepository.findByTitle(title);
    }

    public Service save(Service service) {
        return serviceRepository.save(service);
    }

    public ServiceResponse convertToResponse(Service service) {
        ServiceResponse response = new ServiceResponse();
        response.setId(service.getId());
        response.setTitle(service.getTitle());
        response.setSubtitle(service.getSubtitle());
        response.setCreatedAt(service.getCreatedAt());
        return response;
    }

    public List<ServiceResponse> findAllServiceResponses() {
        return findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<ServiceResponse> getTop3Services() {
        return findAll().stream()
                .limit(3)
                .map(this::convertToResponse)
                .toList();
    }

    public List<ServiceResponse> getAllServicesSortedByTitle() {
        return findAll().stream()
                .sorted((s1, s2) -> s1.getTitle().compareToIgnoreCase(s2.getTitle()))
                .map(this::convertToResponse)
                .toList();
    }

    public List<ServiceResponse> getAllServicesSortedByPopularity() {

        return findAll().stream()
                .sorted((s1, s2) -> Integer.compare(s2.getDoctors().size(), s1.getDoctors().size()))
                .map(this::convertToResponse)
                .toList();
    }
}

