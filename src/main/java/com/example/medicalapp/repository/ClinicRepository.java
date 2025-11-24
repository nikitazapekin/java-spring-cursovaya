package com.example.medicalapp.repository;

import com.example.medicalapp.entity.Clinic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicRepository extends CrudRepository<Clinic, Long> {
    List<Clinic> findByNameContainingIgnoreCase(String name);
}

