package com.example.medicalapp.repository;

import com.example.medicalapp.entity.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Long> {
    Optional<Service> findByTitle(String title);
    List<Service> findAll();
}

