package com.example.medicalapp.repository;



import com.example.medicalapp.entity.Child;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildRepository extends CrudRepository<Child, Long> {
    List<Child> findByParentId(Long parentId);
    List<Child> findByParentUserEmail(String email);
    Optional<Child> findByIdAndParentId(Long id, Long parentId);
    boolean existsByIdAndParentId(Long id, Long parentId);
}