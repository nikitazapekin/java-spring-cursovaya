package com.example.medicalapp.repository;

import com.example.medicalapp.entity.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByChatIdOrderBySentAtAsc(Long chatId);
    List<Message> findTop1ByChatIdOrderBySentAtDesc(Long chatId);

    // Новый метод с JOIN для получения сообщений с информацией о чате и участниках
    @Query("SELECT m FROM Message m JOIN FETCH m.chat c JOIN FETCH c.patient p JOIN FETCH c.doctor d WHERE m.chat.id = :chatId ORDER BY m.sentAt ASC")
    List<Message> findByChatIdWithParticipantsOrderBySentAtAsc(@Param("chatId") Long chatId);
}