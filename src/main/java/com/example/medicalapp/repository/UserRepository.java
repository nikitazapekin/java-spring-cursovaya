package com.example.medicalapp.repository;

import com.example.medicalapp.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<User> findUserById(@Param("userId") Long userId);

    boolean existsById(Long id);
}