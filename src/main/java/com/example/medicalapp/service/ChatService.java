package com.example.medicalapp.service;

import com.example.medicalapp.entity.Chat;
import com.example.medicalapp.entity.Message;
import com.example.medicalapp.models.ChatDTO;
import com.example.medicalapp.models.MessageDTO;
import com.example.medicalapp.repository.ChatRepository;
import com.example.medicalapp.repository.MessageRepository;
import com.example.medicalapp.repository.PatientRepository;
import com.example.medicalapp.repository.DoctorRepository;
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
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Transactional
    public Long getOrCreateChat(Long patientId, Long doctorId, Long authorId) {
        System.out.println("=== Getting or creating chat ===");
        System.out.println("PatientId: " + patientId + ", DoctorId: " + doctorId + ", AuthorId: " + authorId);

        Optional<Chat> existingChat = chatRepository.findByPatientIdAndDoctorId(patientId, doctorId);
        if (existingChat.isPresent()) {
            Long chatId = existingChat.get().getId();
            System.out.println("Existing chat found: " + chatId);
            return chatId;
        }

        Chat chat = new Chat(patientId, doctorId, authorId);
        chat = chatRepository.save(chat);
        System.out.println("New chat created with ID: " + chat.getId());

        return chat.getId();
    }

    @Transactional
    public MessageDTO sendMessage(Long chatId, String messageText, Long fromUser, Long toUser) {
        System.out.println("=== Sending message ===");
        System.out.println("ChatId: " + chatId + ", From: " + fromUser + ", To: " + toUser + ", Message: " + messageText);

        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        if (chatOpt.isPresent()) {
            Chat chat = chatOpt.get();

            Message message = new Message(chat, fromUser, toUser, messageText);
            message = messageRepository.save(message);
            System.out.println("Message saved with ID: " + message.getId());

            return convertMessageToDTO(message);
        }

        throw new RuntimeException("Chat not found with id: " + chatId);
    }

    public List<MessageDTO> getChatHistory(Long chatId) {
        System.out.println("Getting chat history for chatId: " + chatId);
        List<Message> messages = messageRepository.findByChatIdOrderBySentAtAsc(chatId);
        System.out.println("Found " + messages.size() + " messages");

        return messages.stream()
                .map(this::convertMessageToDTO)
                .collect(Collectors.toList());
    }

    public List<ChatDTO> getUserChats(Long userId) {
        System.out.println("Getting user chats for userId: " + userId);
        List<Chat> chats = chatRepository.findByPatientId(userId);
        return chats.stream()
                .map(chat -> convertChatToDTO(chat, "PATIENT"))
                .collect(Collectors.toList());
    }

    public List<ChatDTO> getDoctorChats(Long doctorId) {
        System.out.println("Getting doctor chats for doctorId: " + doctorId);
        List<Chat> chats = chatRepository.findByDoctorId(doctorId);
        return chats.stream()
                .map(chat -> convertChatToDTO(chat, "DOCTOR"))
                .collect(Collectors.toList());
    }

    private ChatDTO convertChatToDTO(Chat chat, String userRole) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setPatientId(chat.getPatientId());
        dto.setDoctorId(chat.getDoctorId());
        dto.setAuthorId(chat.getAuthorId());
        dto.setCreatedAt(chat.getCreatedAt());

        // Получаем последнее сообщение для чата
        List<Message> lastMessages = messageRepository.findTop1ByChatIdOrderBySentAtDesc(chat.getId());
        if (!lastMessages.isEmpty()) {
            Message lastMessage = lastMessages.get(0);
            dto.setLastMessage(lastMessage.getMessageText());
            dto.setLastMessageTime(lastMessage.getSentAt());
        }

        if ("PATIENT".equals(userRole)) {

            dto.setParticipantId(chat.getDoctorId());
            Optional<com.example.medicalapp.entity.Doctor> doctorOpt = doctorRepository.findById(chat.getDoctorId());
            if (doctorOpt.isPresent()) {
                com.example.medicalapp.entity.Doctor doctor = doctorOpt.get();
                dto.setParticipantName("Доктор " + doctor.getFirstName() + " " + doctor.getLastName());
                dto.setChatName("Чат с доктором " + doctor.getFirstName() + " " + doctor.getLastName());
                dto.setAvatar(doctor.getAvatar());
            }
        } else {

            dto.setParticipantId(chat.getPatientId());
            Optional<com.example.medicalapp.entity.Patient> patientOpt = patientRepository.findById(chat.getPatientId());
            if (patientOpt.isPresent()) {
                com.example.medicalapp.entity.Patient patient = patientOpt.get();
                dto.setParticipantName("Пациент " + patient.getFirstName() + " " + patient.getLastName());
                dto.setChatName("Чат с пациентом " + patient.getFirstName() + " " + patient.getLastName());
                dto.setAvatar(patient.getAvatar());
            }
        }

        return dto;
    }

    private MessageDTO convertMessageToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setMessage(message.getMessageText());
        dto.setSenderId(message.getFromUser());
        dto.setReceiverId(message.getToUser());
        dto.setTime(message.getSentAt());
        dto.setIsRead(message.getIsRead());
        dto.setChatId(message.getChat().getId());
        return dto;
    }















}