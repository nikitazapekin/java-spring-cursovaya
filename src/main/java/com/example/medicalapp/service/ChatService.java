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
    private ChatBaseRepository chatBaseRepository;

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

    @Transactional
    public Long getOrCreateUserChat(Long patientId, Long doctorId) {
        System.out.println("=== Getting or creating chat ===");
        System.out.println("PatientId: " + patientId + ", DoctorId: " + doctorId);

        Optional<ChatBase> existingChatBase = chatBaseRepository.findByPatientIdAndDoctorId(patientId, doctorId);
        if (existingChatBase.isPresent()) {
            Long chatId = existingChatBase.get().getId();
            System.out.println("Existing chat found: " + chatId);
            return chatId;
        }

        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);

        if (patientOpt.isPresent() && doctorOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Doctor doctor = doctorOpt.get();

            ChatBase chatBase = new ChatBase(patientId, doctorId);
            chatBase = chatBaseRepository.save(chatBase);
            Long chatId = chatBase.getId();
            System.out.println("Chat base created with ID: " + chatId);

            String userChatName = "Чат с доктором " + doctor.getFirstName() + " " + doctor.getLastName();
            if (userChatName.length() > 255) {
                userChatName = userChatName.substring(0, 255);
            }

            String doctorAvatar = doctor.getAvatar();
            if (doctorAvatar != null && doctorAvatar.length() > 500) {
                doctorAvatar = doctorAvatar.substring(0, 500);
            }

            UserChats userChat = new UserChats(chatBase, patient, userChatName, doctorAvatar);
            userChatsRepository.save(userChat);
            System.out.println("User chat created with ID: " + chatId);
            String doctorChatName = "Чат с пациентом " + patient.getFirstName() + " " + patient.getLastName();
            if (doctorChatName.length() > 255) {
                doctorChatName = doctorChatName.substring(0, 255);
            }

            String patientAvatar = patient.getAvatar();
            if (patientAvatar != null && patientAvatar.length() > 500) {
                patientAvatar = patientAvatar.substring(0, 500);
            }

            DoctorChats doctorChat = new DoctorChats(chatBase, doctor, doctorChatName, patientAvatar);
            doctorChatsRepository.save(doctorChat);
            System.out.println("Doctor chat created with ID: " + chatId);

            return chatId;
        }

        System.out.println("ERROR: Patient or Doctor not found. PatientId: " + patientId + ", DoctorId: " + doctorId);
        throw new RuntimeException("Patient or Doctor not found");
    }

    @Transactional
    public MessageDTO sendMessageToUserChat(Long chatId, String message, String senderType, Long senderId) {
        System.out.println("=== Sending message to USER chat ===");
        System.out.println("ChatId: " + chatId + ", Message: " + message + ", Sender: " + senderId + ", Type: " + senderType);

        Optional<UserChats> chatOpt = userChatsRepository.findById(chatId);
        if (chatOpt.isPresent()) {
            UserChats chat = chatOpt.get();
            System.out.println("User chat found: " + chat.getId());

            UserMessages userMessage = new UserMessages(chat, message, senderType, senderId);
            userMessage = userMessagesRepository.save(userMessage);
            System.out.println("User message saved with ID: " + userMessage.getId());

            chat.setLastMessage(message);
            chat.setLastMessageTime(LocalDateTime.now());
            userChatsRepository.save(chat);

            Optional<DoctorChats> doctorChatOpt = doctorChatsRepository.findById(chatId);
            if (doctorChatOpt.isPresent()) {
                DoctorChats doctorChat = doctorChatOpt.get();
                doctorChat.setLastMessage(message);
                doctorChat.setLastMessageTime(LocalDateTime.now());
                doctorChatsRepository.save(doctorChat);

                DoctorMessages doctorMessage = new DoctorMessages(doctorChat, message, senderType, senderId);
                doctorMessagesRepository.save(doctorMessage);
                System.out.println("Doctor message saved with ID: " + doctorMessage.getId());
            } else {
                System.out.println("WARNING: Corresponding doctor chat not found for chatId: " + chatId);
            }

            return convertUserMessageToDTO(userMessage);
        }
        System.out.println("ERROR: User chat not found with id: " + chatId);
        throw new RuntimeException("User chat not found with id: " + chatId);
    }

    @Transactional
    public MessageDTO sendMessageToDoctorChat(Long chatId, String message, String senderType, Long senderId) {
        System.out.println("=== Sending message to DOCTOR chat ===");
        System.out.println("ChatId: " + chatId + ", Message: " + message + ", Sender: " + senderId + ", Type: " + senderType);

        Optional<DoctorChats> chatOpt = doctorChatsRepository.findById(chatId);
        if (chatOpt.isPresent()) {
            DoctorChats chat = chatOpt.get();
            System.out.println("Doctor chat found: " + chat.getId());

            DoctorMessages doctorMessage = new DoctorMessages(chat, message, senderType, senderId);
            doctorMessage = doctorMessagesRepository.save(doctorMessage);
            System.out.println("Doctor message saved with ID: " + doctorMessage.getId());

            chat.setLastMessage(message);
            chat.setLastMessageTime(LocalDateTime.now());
            doctorChatsRepository.save(chat);

            Optional<UserChats> userChatOpt = userChatsRepository.findById(chatId);
            if (userChatOpt.isPresent()) {
                UserChats userChat = userChatOpt.get();
                userChat.setLastMessage(message);
                userChat.setLastMessageTime(LocalDateTime.now());
                userChatsRepository.save(userChat);


                UserMessages userMessage = new UserMessages(userChat, message, senderType, senderId);
                userMessagesRepository.save(userMessage);

            } else {
                System.out.println("WARNING: Corresponding user chat not found for chatId: " + chatId);
            }

            return convertDoctorMessageToDTO(doctorMessage);
        }

        throw new RuntimeException("Doctor chat not found with id: " + chatId);
    }

    public List<ChatDTO> getUserChats(Long patientId) {
        List<UserChats> chats = userChatsRepository.findByPatientId(patientId);
        return chats.stream().map(this::convertUserChatToDTO).collect(Collectors.toList());
    }

    public List<ChatDTO> getDoctorChats(Long doctorId) {
        List<DoctorChats> chats = doctorChatsRepository.findByDoctorId(doctorId);
        return chats.stream().map(this::convertDoctorChatToDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> getUserChatMessages(Long chatId) {
        List<UserMessages> messages = userMessagesRepository.findByChatIdOrderByTimeAsc(chatId);
        return messages.stream().map(this::convertUserMessageToDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> getDoctorChatMessages(Long chatId) {
        List<DoctorMessages> messages = doctorMessagesRepository.findByChatIdOrderByTimeAsc(chatId);
        return messages.stream().map(this::convertDoctorMessageToDTO).collect(Collectors.toList());
    }


    private ChatDTO convertUserChatToDTO(UserChats chat) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setChatName(chat.getChatName());
        dto.setLastMessage(chat.getLastMessage());
        dto.setLastMessageTime(chat.getLastMessageTime());
        dto.setAvatar(chat.getAvatar());
        dto.setParticipantId(chat.getDoctorId());

        if (chat.getDoctorId() != null) {
            Optional<Doctor> doctorOpt = doctorRepository.findById(chat.getDoctorId());
            if (doctorOpt.isPresent()) {
                Doctor doctor = doctorOpt.get();
                dto.setParticipantName(doctor.getFirstName() + " " + doctor.getLastName());
            }
        }

        return dto;
    }

    private ChatDTO convertDoctorChatToDTO(DoctorChats chat) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setChatName(chat.getChatName());
        dto.setLastMessage(chat.getLastMessage());
        dto.setLastMessageTime(chat.getLastMessageTime());
        dto.setAvatar(chat.getAvatar());
        dto.setParticipantId(chat.getPatientId());

        if (chat.getPatientId() != null) {
            Optional<Patient> patientOpt = patientRepository.findById(chat.getPatientId());
            if (patientOpt.isPresent()) {
                Patient patient = patientOpt.get();
                dto.setParticipantName(patient.getFirstName() + " " + patient.getLastName());
            }
        }

        return dto;
    }

    private MessageDTO convertUserMessageToDTO(UserMessages message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setMessage(message.getMessage());
        dto.setFrom(message.getSender());
        dto.setSenderId(message.getSenderId());
        dto.setTime(message.getTime());
        dto.setIsRead(message.getIsRead());
        dto.setChatId(message.getChat().getId());
        dto.setType("user");
        return dto;
    }

    private MessageDTO convertDoctorMessageToDTO(DoctorMessages message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setMessage(message.getMessage());
        dto.setFrom(message.getSender());
        dto.setSenderId(message.getSenderId());
        dto.setTime(message.getTime());
        dto.setIsRead(message.getIsRead());
        dto.setChatId(message.getChat().getId());
        dto.setType("doctor");
        return dto;
    }
}