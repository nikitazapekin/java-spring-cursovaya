package com.example.medicalapp.repository;

import com.example.medicalapp.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {
    
    @Query("SELECT d FROM Drug d WHERE " +
           "(:searchQuery IS NULL OR :searchQuery = '' OR " +
           "LOWER(d.title) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
           "LOWER(d.type) LIKE LOWER(CONCAT('%', :searchQuery, '%')))")
    List<Drug> findDrugsWithSearch(@Param("searchQuery") String searchQuery);
}

