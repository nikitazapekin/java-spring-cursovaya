package com.example.weather.repository;


import com.example.weather.entity.ResidentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentTypeRepository extends CrudRepository<ResidentType, Long> {
    Optional<ResidentType> findByName(String name);

    @Query("SELECT rt FROM ResidentType rt WHERE rt.communicationLanguage = :language")
    List<ResidentType> findByCommunicationLanguage(@Param("language") String language);

    boolean existsByName(String name);
}