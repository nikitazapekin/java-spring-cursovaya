package com.example.medicalapp.repository;


import com.example.medicalapp.entity.ChildIdentifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChildIdentifierRepository extends CrudRepository<ChildIdentifier, Long> {
    Optional<ChildIdentifier> findByChildId(Long childId);
    Optional<ChildIdentifier> findByIdentifier(String identifier);
    boolean existsByIdentifier(String identifier);
}