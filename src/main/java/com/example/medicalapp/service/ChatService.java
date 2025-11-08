package com.example.medicalapp.service;

import com.example.medicalapp.entity.*;
import com.example.medicalapp.models.ChatDTO;
import com.example.medicalapp.models.MessageDTO;
import com.example.medicalapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private UserChatsRepository userChatsRepository;

    @Autowired
    private DoctorChatsRepository doctorChatsRepository;

    @Autowired
    private UserMessagesRepository userMessagesRepository;

    @Autowired
    private DoctorMessagesRepository doctorMessagesRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public List<ChatDTO> getUserChats(Long patientId) {
        List<UserChats> chats = userChatsRepository.findByPatientId(patientId);
        return chats.stream().map(this::convertToChatDTO).collect(Collectors.toList());
    }

    public List<ChatDTO> getDoctorChats(Long doctorId) {
        List<DoctorChats> chats = doctorChatsRepository.findByDoctorId(doctorId);
        return chats.stream().map(this::convertToChatDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> getUserChatMessages(Long chatId) {
        List<UserMessages> messages = userMessagesRepository.findByChatIdOrderByTimeAsc(chatId);
        return messages.stream().map(this::convertToMessageDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> getDoctorChatMessages(Long chatId) {
        List<DoctorMessages> messages = doctorMessagesRepository.findByChatIdOrderByTimeAsc(chatId);
        return messages.stream().map(this::convertToMessageDTO).collect(Collectors.toList());
    }

    @Transactional
    public MessageDTO sendUserMessage(Long chatId, String message, String from) {
        Optional<UserChats> chatOpt = userChatsRepository.findById(chatId);
        if (chatOpt.isPresent()) {
            UserChats chat = chatOpt.get();
            UserMessages userMessage = new UserMessages(chat, message, from);
            userMessage = userMessagesRepository.save(userMessage);

            // Update last message in chat
            chat.setLastMessage(message);
            chat.setLastMessageTime(LocalDateTime.now());
            userChatsRepository.save(chat);

            return convertToMessageDTO(userMessage);
        }
        throw new RuntimeException("Chat not found");
    }

    @Transactional
    public MessageDTO sendDoctorMessage(Long chatId, String message, String from) {
        Optional<DoctorChats> chatOpt = doctorChatsRepository.findById(chatId);
        if (chatOpt.isPresent()) {
            DoctorChats chat = chatOpt.get();
            DoctorMessages doctorMessage = new DoctorMessages(chat, message, from);
            doctorMessage = doctorMessagesRepository.save(doctorMessage);


            chat.setLastMessage(message);
            chat.setLastMessageTime(LocalDateTime.now());
            doctorChatsRepository.save(chat);

            return convertToMessageDTO(doctorMessage);
        }
        throw new RuntimeException("Chat not found");
    }

    public Long getOrCreateUserChat(Long patientId, Long doctorId) {
        Optional<UserChats> existingChat = userChatsRepository.findByPatientIdAndDoctorId(patientId, doctorId);
        if (existingChat.isPresent()) {
            return existingChat.get().getId();
        }

        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);

        if (patientOpt.isPresent() && doctorOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Doctor doctor = doctorOpt.get();

            String chatName = "Чат с доктором " + doctor.getFirstName() + " " + doctor.getLastName();
            UserChats newChat = new UserChats(patient, chatName, doctor.getAvatar(), doctorId);
            newChat = userChatsRepository.save(newChat);

            // Create corresponding doctor chat
            String doctorChatName = "Чат с пациентом " + patient.getFirstName() + " " + patient.getLastName();
            DoctorChats doctorChat = new DoctorChats(doctor, doctorChatName, patient.getAvatar(), patientId);
            doctorChatsRepository.save(doctorChat);

            return newChat.getId();
        }
        throw new RuntimeException("Patient or Doctor not found");
    }

    private ChatDTO convertToChatDTO(UserChats chat) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setChatName(chat.getChatName());
        dto.setLastMessage(chat.getLastMessage());
        dto.setLastMessageTime(chat.getLastMessageTime());
        dto.setAvatar(chat.getAvatar());
        dto.setParticipantId(chat.getDoctorId());
        return dto;
    }

    private ChatDTO convertToChatDTO(DoctorChats chat) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setChatName(chat.getChatName());
        dto.setLastMessage(chat.getLastMessage());
        dto.setLastMessageTime(chat.getLastMessageTime());
        dto.setAvatar(chat.getAvatar());
        dto.setParticipantId(chat.getPatientId());
        return dto;
    }

    private MessageDTO convertToMessageDTO(UserMessages message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setMessage(message.getMessage());
        dto.setFrom(message.getFrom());
        dto.setTime(message.getTime());
        dto.setIsRead(message.getIsRead());
        dto.setChatId(message.getChat().getId());
        dto.setType("user");
        return dto;
    }

    private MessageDTO convertToMessageDTO(DoctorMessages message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setMessage(message.getMessage());
        dto.setFrom(message.getFrom());
        dto.setTime(message.getTime());
        dto.setIsRead(message.getIsRead());
        dto.setChatId(message.getChat().getId());
        dto.setType("doctor");
        return dto;
    }
}