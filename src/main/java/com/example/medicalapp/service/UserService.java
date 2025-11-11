package com.example.medicalapp.service;

import com.example.medicalapp.entity.Doctor;
import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.entity.User;
import com.example.medicalapp.models.UserProfileResponse;
import com.example.medicalapp.repository.DoctorRepository;
import com.example.medicalapp.repository.PatientRepository;
import com.example.medicalapp.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public UserProfileResponse getUserProfileById(Long userId) {
        System.out.println("Getting user profile for ID: " + userId);

        // Находим пользователя
        Optional<User> userOpt = userProfileRepository.findById(userId);
        if (userOpt.isEmpty()) {
            System.out.println("User not found with ID: " + userId);
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = userOpt.get();
        System.out.println("Found user: " + user.getEmail() + " with role: " + user.getRole());

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());

        // В зависимости от роли заполняем соответствующие поля
        if ("PATIENT".equals(user.getRole())) {
            fillPatientData(response, user);
        } else if ("DOCTOR".equals(user.getRole())) {
            fillDoctorData(response, user);
        } else {
            System.out.println("Unknown user role: " + user.getRole());
            throw new RuntimeException("Unknown user role: " + user.getRole());
        }

        return response;
    }

    private void fillPatientData(UserProfileResponse response, User user) {
        Optional<Patient> patientOpt = patientRepository.findByUserId(user.getId());
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            response.setFirstName(patient.getFirstName());
            response.setLastName(patient.getLastName());
            response.setAvatar(patient.getAvatar());
            response.setPhoneNumber(patient.getPhoneNumber());
            response.setRegion(patient.getRegion());
            response.setCitate(patient.getCitate());
            response.setRegistrationDate(patient.getRegistrationDate());
            System.out.println("Patient info found: " + patient.getFirstName() + " " + patient.getLastName());
        } else {
            System.out.println("Patient profile not found for user ID: " + user.getId());
            throw new RuntimeException("Patient profile not found for user id: " + user.getId());
        }
    }

    private void fillDoctorData(UserProfileResponse response, User user) {
        Optional<Doctor> doctorOpt = doctorRepository.findByUserId(user.getId());
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            response.setFirstName(doctor.getFirstName());
            response.setLastName(doctor.getLastName());
            response.setAvatar(doctor.getAvatar());
            response.setRate(doctor.getRate());
            response.setStatus(doctor.getStatus());
            response.setCitate(doctor.getCitate());
            response.setExperience(doctor.getExperience());
            response.setEducation(doctor.getEducation());
            response.setSpecialization(doctor.getSpecialization());
            response.setAchievements(doctor.getAchievements());
            response.setIncrementQualification(doctor.getIncrementQualification());
            System.out.println("Doctor info found: " + doctor.getFirstName() + " " + doctor.getLastName());
        } else {
            System.out.println("Doctor profile not found for user ID: " + user.getId());
            throw new RuntimeException("Doctor profile not found for user id: " + user.getId());
        }
    }

    public UserProfileResponse getUserProfileByEmail(String email) {
        Optional<User> userOpt = userProfileRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return getUserProfileById(userOpt.get().getId());
    }
}