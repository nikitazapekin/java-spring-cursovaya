package com.example.medicalapp.repository;

import com.example.medicalapp.entity.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findByChatIdOrderBySentAtAsc(Long chatId);
    List<Message> findTop1ByChatIdOrderBySentAtDesc(Long chatId);
}