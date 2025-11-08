package com.example.medicalapp.repository;

import com.example.medicalapp.entity.DoctorMessages;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorMessagesRepository extends CrudRepository<DoctorMessages, Long> {
    List<DoctorMessages> findByChatIdOrderByTimeAsc(Long chatId);
}