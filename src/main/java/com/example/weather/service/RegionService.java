package com.example.weather.service;


import com.example.weather.entity.Region;
import com.example.weather.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<Region> findAll() {
        return (List<Region>) regionRepository.findAll();
    }

    public Optional<Region> findById(Long id) {
        return regionRepository.findById(id);
    }

    public Region save(Region region) {
        return regionRepository.save(region);
    }

    public void deleteById(Long id) {
        regionRepository.deleteById(id);
    }

    public Optional<Region> findByName(String name) {
        return regionRepository.findByName(name);
    }

    public List<Region> findByAreaGreaterThan(Double area) {
        return regionRepository.findByAreaGreaterThan(area);
    }

    public List<Region> findByResidentLanguage(String language) {
        return regionRepository.findByResidentLanguage(language);
    }

    public List<Region> findByResidentTypeId(Long residentTypeId) {
        return regionRepository.findByResidentTypeId(residentTypeId);
    }

    public boolean existsByName(String name) {
        return regionRepository.existsByName(name);
    }
}