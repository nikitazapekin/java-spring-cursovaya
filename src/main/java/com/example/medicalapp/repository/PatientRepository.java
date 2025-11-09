package com.example.medicalapp.repository;

import com.example.medicalapp.entity.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {
    Optional<Patient> findByUserEmail(String email);
    boolean existsByUserEmail(String email);


    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
     List<Patient> findByFirstNameContainingIgnoreCase(String firstName);

    List<Patient> findByLastNameContainingIgnoreCase(String lastName);
}