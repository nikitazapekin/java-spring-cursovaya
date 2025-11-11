package com.example.medicalapp.repository;


import com.example.medicalapp.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);


    Optional<User> findById(Long id);

}