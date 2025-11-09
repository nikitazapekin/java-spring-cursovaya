package com.example.medicalapp.repository;


import com.example.medicalapp.entity.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Long> {
    Optional<Doctor> findByUserEmail(String email);
    boolean existsByUserEmail(String email);


    List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    List<Doctor> findByFirstNameContainingIgnoreCase(String firstName);

    List<Doctor> findByLastNameContainingIgnoreCase(String lastName);
}