package com.example.medicalapp.controller;

import com.example.medicalapp.entity.Advice;
import com.example.medicalapp.models.AdviceResponse;
import com.example.medicalapp.service.AdviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/advice")
public class AdviceController {

    @Autowired
    private AdviceService adviceService;

    @GetMapping
    public ResponseEntity<?> getAllAdvice() {
        try {
            List<AdviceResponse> adviceList = adviceService.findAllAdviceResponses();
            return ResponseEntity.ok(adviceList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving advice: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdviceById(@PathVariable Long id) {
        try {
            Optional<AdviceResponse> adviceResponseOpt = adviceService.findAdviceResponseById(id);

            if (adviceResponseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Advice not found\"}");
            }

            return ResponseEntity.ok(adviceResponseOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving advice: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getAdviceByType(@PathVariable String type) {
        try {
            List<AdviceResponse> adviceList = adviceService.findAdviceResponsesByType(type);
            return ResponseEntity.ok(adviceList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving advice by type: " + e.getMessage() + "\"}");
        }
    }



    @GetMapping("/random")
    public ResponseEntity<?> getRandomAdvice() {
        try {
            Optional<AdviceResponse> randomAdvice = adviceService.findRandomAdvice();

            if (randomAdvice.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"No advice available\"}");
            }

            return ResponseEntity.ok(randomAdvice.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving random advice: " + e.getMessage() + "\"}");
        }
    }


    @GetMapping("/random/{type}")
    public ResponseEntity<?> getRandomAdviceByType(@PathVariable String type) {
        try {
            Optional<AdviceResponse> randomAdvice = adviceService.findRandomAdviceByType(type);

            if (randomAdvice.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"No advice available for type: " + type + "\"}");
            }

            return ResponseEntity.ok(randomAdvice.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error retrieving random advice by type: " + e.getMessage() + "\"}");
        }
    }


    @PostMapping
    public ResponseEntity<?> createAdvice(@RequestBody Advice advice) {
        try {
            Advice savedAdvice = adviceService.save(advice);
            AdviceResponse response = adviceService.convertToResponse(savedAdvice);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error creating advice: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdvice(@PathVariable Long id, @RequestBody Advice advice) {
        try {
            if (!adviceService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Advice not found\"}");
            }

            advice.setId(id);
            Advice updatedAdvice = adviceService.save(advice);
            AdviceResponse response = adviceService.convertToResponse(updatedAdvice);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error updating advice: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdvice(@PathVariable Long id) {
        try {
            if (!adviceService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"Advice not found\"}");
            }

            adviceService.deleteById(id);
            return ResponseEntity.ok("{\"message\": \"Advice deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error deleting advice: " + e.getMessage() + "\"}");
        }
    }
}