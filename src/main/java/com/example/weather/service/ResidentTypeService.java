package com.example.weather.service;


import com.example.weather.entity.ResidentType;
import com.example.weather.repository.ResidentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidentTypeService {

    @Autowired
    private ResidentTypeRepository residentTypeRepository;

    public List<ResidentType> findAll() {
        return (List<ResidentType>) residentTypeRepository.findAll();
    }

    public Optional<ResidentType> findById(Long id) {
        return residentTypeRepository.findById(id);
    }

    public ResidentType save(ResidentType residentType) {
        return residentTypeRepository.save(residentType);
    }

    public void deleteById(Long id) {
        residentTypeRepository.deleteById(id);
    }

    public Optional<ResidentType> findByName(String name) {
        return residentTypeRepository.findByName(name);
    }

    public List<ResidentType> findByCommunicationLanguage(String language) {
        return residentTypeRepository.findByCommunicationLanguage(language);
    }

    public boolean existsByName(String name) {
        return residentTypeRepository.existsByName(name);
    }
}