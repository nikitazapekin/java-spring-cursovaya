package com.example.medicalapp.service;

import com.example.medicalapp.entity.Doctor;
import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.entity.User;
import com.example.medicalapp.helpers.PasswordEncoderService;
import com.example.medicalapp.models.AuthRequest;
import com.example.medicalapp.models.AuthResponse;
import com.example.medicalapp.models.RegisterRequest;
import com.example.medicalapp.repository.DoctorRepository;
import com.example.medicalapp.repository.PatientRepository;
import com.example.medicalapp.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserProfileRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoderService passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "PATIENT");
        user = userRepository.save(user);

        String userType = "DOCTOR".equalsIgnoreCase(user.getRole()) ? "DOCTOR" : "PATIENT";

        if ("DOCTOR".equalsIgnoreCase(user.getRole())) {
            Doctor doctor = new Doctor(user, request.getFirstName(), request.getLastName());
            doctor.setAvatar("doctorDefault.png"); // Устанавливаем дефолтный аватар для врача
            doctorRepository.save(doctor);
        } else {
            Patient patient = new Patient(user, request.getFirstName(), request.getLastName(), request.getPhoneNumber());
            patientRepository.save(patient);
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole(), userType);
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getRole(), userType);


        AuthResponse response = new AuthResponse(accessToken, user.getRole(), user.getEmail(), user.getId());
        return response;
    }

    public AuthResponse login(AuthRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty() ||
                !passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            return new AuthResponse("Invalid email or password");
        }

        User user = userOptional.get();
        String userType = "DOCTOR".equalsIgnoreCase(user.getRole()) ? "DOCTOR" : "PATIENT";

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole(), userType);
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getRole(), userType);


        AuthResponse response = new AuthResponse(accessToken, user.getRole(), user.getEmail(), user.getId());
        return response;
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            return new AuthResponse("Invalid or expired refresh token");
        }

        String email = jwtService.getEmailFromToken(refreshToken);
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return new AuthResponse("User not found");
        }

        User user = userOptional.get();
        String userType = jwtService.getUserTypeFromToken(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole(), userType);

        return new AuthResponse(newAccessToken, user.getRole(), user.getEmail(), user.getId());
    }

    public boolean validateAccessToken(String accessToken) {
        return jwtService.isAccessTokenValid(accessToken);
    }

    public String getUserTypeFromToken(String token) {
        return jwtService.getUserTypeFromToken(token);
    }

    public boolean isDoctor(String token) {
        return "DOCTOR".equals(getUserTypeFromToken(token));
    }

    public boolean isPatient(String token) {
        return "PATIENT".equals(getUserTypeFromToken(token));
    }
}