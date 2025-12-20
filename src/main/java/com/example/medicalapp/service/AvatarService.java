package com.example.medicalapp.service;

import com.example.medicalapp.entity.Doctor;
import com.example.medicalapp.entity.Patient;
import com.example.medicalapp.entity.User;
import com.example.medicalapp.models.AvatarResponse;
import com.example.medicalapp.repository.DoctorRepository;
import com.example.medicalapp.repository.PatientRepository;
import com.example.medicalapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AvatarService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public Optional<AvatarResponse> getUserAvatar(Long userId) {

        if (!userRepository.existsById(userId)) {
            return Optional.empty();
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        AvatarResponse response = new AvatarResponse();
        response.setUserId(userId);
        response.setEmail(user.getEmail());
        response.setUserType(user.getRole());

        if ("DOCTOR".equalsIgnoreCase(user.getRole())) {
            return getDoctorAvatar(user, response);
        } else if ("PATIENT".equalsIgnoreCase(user.getRole())) {
            return getPatientAvatar(user, response);
        } else {

            return Optional.empty();
        }
    }

    private Optional<AvatarResponse> getDoctorAvatar(User user, AvatarResponse response) {
        Optional<Doctor> doctorOpt = doctorRepository.findByUser(user);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            response.setAvatar(doctor.getAvatar());
            response.setFullName(doctor.getLastName() + " " +
                    doctor.getFirstName() + " " +
                    (doctor.getMiddleName() != null ? doctor.getMiddleName() : ""));
            return Optional.of(response);
        }
        return Optional.empty();
    }

    private Optional<AvatarResponse> getPatientAvatar(User user, AvatarResponse response) {
        Optional<Patient> patientOpt = patientRepository.findByUser(user);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            response.setAvatar(patient.getAvatar());
            response.setFullName(patient.getLastName() + " " + patient.getFirstName());
            return Optional.of(response);
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public String getAvatarUrl(Long userId) {
        Optional<AvatarResponse> avatarOpt = getUserAvatar(userId);
        if (avatarOpt.isPresent()) {
            return avatarOpt.get().getAvatar();
        }
        return null;
    }
}