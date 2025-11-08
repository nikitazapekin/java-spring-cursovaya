package com.example.medicalapp.repository;

import com.example.medicalapp.entity.UserMessages;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMessagesRepository extends CrudRepository<UserMessages, Long> {
    List<UserMessages> findByChatIdOrderByTimeAsc(Long chatId);
}