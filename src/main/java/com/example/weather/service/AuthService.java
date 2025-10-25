package com.example.weather.service;



import com.example.weather.entity.Doctor;
import com.example.weather.entity.Patient;
import com.example.weather.entity.User;
import com.example.weather.helpers.PasswordEncoderService;
import com.example.weather.models.AuthRequest;
import com.example.weather.models.AuthResponse;
import com.example.weather.models.RegisterRequest;
import com.example.weather.repository.DoctorRepository;
import com.example.weather.repository.PatientRepository;
import com.example.weather.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

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

        // Create User
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "PATIENT");
        user = userRepository.save(user);

        // Create corresponding profile based on role
        if ("DOCTOR".equalsIgnoreCase(user.getRole())) {
            Doctor doctor = new Doctor(user, request.getFirstName(), request.getLastName());
            doctorRepository.save(doctor);
        } else {
            Patient patient = new Patient(user, request.getFirstName(), request.getLastName(), request.getPhoneNumber());
            patientRepository.save(patient);
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        AuthResponse response = new AuthResponse(accessToken, user.getRole(), user.getEmail());
        return response;
    }

    public AuthResponse login(AuthRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty() ||
                !passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            return new AuthResponse("Invalid email or password");
        }

        User user = userOptional.get();
        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        AuthResponse response = new AuthResponse(accessToken, user.getRole(), user.getEmail());
        return response;
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
            return new AuthResponse("Invalid refresh token");
        }

        // Проверяем, что это именно refresh token
        if (!"refresh".equals(jwtService.getTokenType(refreshToken))) {
            return new AuthResponse("Invalid token type");
        }

        String email = jwtService.getEmailFromToken(refreshToken);
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return new AuthResponse("User not found");
        }

        User user = userOptional.get();
        String newAccessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole());

        return new AuthResponse(newAccessToken, user.getRole(), user.getEmail());
    }

    public boolean validateAccessToken(String accessToken) {
        if (!jwtService.validateToken(accessToken)) {
            return false;
        }

        // Проверяем, что это именно access token
        return "access".equals(jwtService.getTokenType(accessToken));
    }
}