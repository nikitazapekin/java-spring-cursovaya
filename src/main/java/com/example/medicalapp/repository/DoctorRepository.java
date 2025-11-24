package com.example.medicalapp.repository;


import com.example.medicalapp.entity.Doctor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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

    Optional<Doctor> findByUserId(Long userId);

    List<Doctor> findByMiddleNameContainingIgnoreCase(String middleName);

    List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCase(
            String firstName, String lastName, String middleName);

    List<Doctor> findByRateBetweenOrderByRateDesc(Double minRate, Double maxRate);

    @Query("SELECT d FROM Doctor d JOIN d.services s WHERE s.id = :serviceId")
    List<Doctor> findDoctorsByServiceId(@Param("serviceId") Long serviceId);

}