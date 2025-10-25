package com.example.weather.repository;

import com.example.weather.entity.Region;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends CrudRepository<Region, Long> {
    Optional<Region> findByName(String name);
    List<Region> findByAreaGreaterThan(Double area);

    @Query("SELECT r FROM Region r WHERE r.residentType.communicationLanguage = :language")
    List<Region> findByResidentLanguage(@Param("language") String language);

    @Query("SELECT r FROM Region r WHERE r.residentType.id = :residentTypeId")
    List<Region> findByResidentTypeId(@Param("residentTypeId") Long residentTypeId);

    boolean existsByName(String name);
}