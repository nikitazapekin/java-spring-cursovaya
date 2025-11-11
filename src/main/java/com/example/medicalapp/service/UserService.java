package com.example.medicalapp.service;

import com.example.medicalapp.entity.Doctor;
import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.entity.User;
import com.example.medicalapp.models.UserInfoDTO;
import com.example.medicalapp.repository.DoctorRepository;
import com.example.medicalapp.repository.PatientRepository;
import com.example.medicalapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public UserInfoDTO getUserInfo(Long userId) {
        System.out.println("Getting user info for ID: " + userId);

        // Сначала ищем пользователя
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            System.out.println("User not found with ID: " + userId);
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = userOpt.get();
        System.out.println("Found user: " + user.getEmail() + " with role: " + user.getRole());

        UserInfoDTO userInfo = new UserInfoDTO();
        userInfo.setId(user.getId());
        userInfo.setEmail(user.getEmail());
        userInfo.setRole(user.getRole());

        // В зависимости от роли получаем дополнительную информацию
        if ("PATIENT".equals(user.getRole())) {
            Optional<Patient> patientOpt = patientRepository.findByUserId(userId);
            if (patientOpt.isPresent()) {
                Patient patient = patientOpt.get();
                userInfo.setFirstName(patient.getFirstName());
                userInfo.setLastName(patient.getLastName());
                userInfo.setAvatar(patient.getAvatar());
                System.out.println("Patient info found: " + patient.getFirstName() + " " + patient.getLastName());
            }
        } else if ("DOCTOR".equals(user.getRole())) {
            Optional<Doctor> doctorOpt = doctorRepository.findByUserId(userId);
            if (doctorOpt.isPresent()) {
                Doctor doctor = doctorOpt.get();
                userInfo.setFirstName(doctor.getFirstName());
                userInfo.setLastName(doctor.getLastName());
                userInfo.setAvatar(doctor.getAvatar());
                System.out.println("Doctor info found: " + doctor.getFirstName() + " " + doctor.getLastName());
            }
        } else {
            System.out.println("Unknown user role: " + user.getRole());
        }

        return userInfo;
    }


    // Дополнительные методы для удобства
    public String getUserAvatar(Long userId) {
        UserInfoDTO userInfo = getUserInfo(userId);
        return userInfo.getAvatar();
    }

    public String getUserFullName(Long userId) {
        UserInfoDTO userInfo = getUserInfo(userId);
        return userInfo.getFirstName() + " " + userInfo.getLastName();
    }
}