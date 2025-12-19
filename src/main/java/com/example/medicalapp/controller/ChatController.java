package com.example.medicalapp.controller;

import com.example.medicalapp.entity.Chat;
import com.example.medicalapp.models.ChatDTO;
import com.example.medicalapp.models.MessageDTO;
import com.example.medicalapp.service.ChatService;
import com.example.medicalapp.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private DoctorService doctorService;


    @PostMapping("/start")
    public ResponseEntity<Long> startChat(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam Long authorId ,
            @RequestParam Boolean isDoctor
            ) {

        System.out.println("GOOOOOOOOOOOOD");
        System.out.println("то доктор");
        System.out.println(isDoctor );
        System.out.println("пациент");
        System.out.println(patientId );
        Long id = isDoctor  ?  doctorService.getDoctorIdByUserId(doctorId) : doctorId;
        System.out.println("доктор");

        System.out.println(id );
        System.out.println("автор");
        System.out.println(authorId );

        try {


            Long chatId = chatService.getOrCreateChat(patientId, id, authorId);
            System.out.println("GOOOOOOOOOOOOD");
            System.out.println(patientId );
            System.out.println(doctorId );
            System.out.println(authorId );

            return ResponseEntity.ok(chatId);
        } catch (Exception e) {

            System.out.println("Errrrrrrrrrrrrrrrrrrrrrrrrrrr");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageDTO>> getChatHistory(@PathVariable Long chatId) {
        try {
            List<MessageDTO> messages = chatService.getChatHistory(chatId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatDTO>> getUserChats(@PathVariable Long userId) {
        try {
            List<ChatDTO> chats = chatService.getUserChats(userId);
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }



    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ChatDTO>> getDoctorChats(@PathVariable Long doctorId) {
        try {
           Long id =  doctorService.getDoctorIdByUserId(doctorId);
           System.out.println("SELECTED ID .....................");

            System.out.println(id);
            System.out.println(doctorId);
            List<ChatDTO> chats = chatService.getDoctorChats(id);


            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

