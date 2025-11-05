package com.example.medicalapp.repository;

import com.example.medicalapp.entity.Advice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdviceRepository extends CrudRepository<Advice, Long> {
    List<Advice> findByType(String type);
    Optional<Advice> findById(Long id);
    boolean existsByType(String type);
}