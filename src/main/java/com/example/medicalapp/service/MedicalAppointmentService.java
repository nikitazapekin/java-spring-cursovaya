package com.example.medicalapp.service;

import com.example.medicalapp.entity.Doctor;
import com.example.medicalapp.entity.MedicalAppointment;
import com.example.medicalapp.entity.MedicalCard;
import com.example.medicalapp.entity.Service;
import com.example.medicalapp.models.MedicalAppointmentRequest;
import com.example.medicalapp.models.MedicalAppointmentResponse;
import com.example.medicalapp.repository.DoctorRepository;
import com.example.medicalapp.repository.MedicalAppointmentRepository;
import com.example.medicalapp.repository.MedicalCardRepository;
import com.example.medicalapp.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class MedicalAppointmentService {

    @Autowired
    private MedicalAppointmentRepository medicalAppointmentRepository;

    @Autowired
    private MedicalCardRepository medicalCardRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ServiceService serviceService;

    public List<MedicalAppointment> findByMedicalCardId(Long medicalCardId) {
        return medicalAppointmentRepository.findByMedicalCardId(medicalCardId);
    }

    public Optional<MedicalAppointment> findById(Long id) {
        return medicalAppointmentRepository.findById(id);
    }

    public MedicalAppointment createAppointment(MedicalAppointmentRequest request) throws Exception {
        MedicalCard medicalCard = medicalCardRepository.findById(request.getMedicalCardId())
                .orElseThrow(() -> new Exception("Medical card not found"));

        Doctor doctor = null;
        if (request.getDoctorId() != null) {
            doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new Exception("Doctor not found"));
        }

        Service service = null;
        if (request.getServiceId() != null) {
            service = serviceRepository.findById(request.getServiceId())
                    .orElse(null);
        }

        if (service == null) {
            service = serviceRepository.findByTitle("Консультация")
                    .orElseThrow(() -> new Exception("Default service 'Консультация' not found"));
        }

        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setMedicalCard(medicalCard);
        appointment.setDoctor(doctor);
        appointment.setService(service);
        appointment.setAppointmentName(request.getAppointmentName());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setDescription(request.getDescription());
        appointment.setAppointmentType(request.getAppointmentType());

        if (doctor != null) {
            String doctorInitials = doctor.getLastName() + " " +
                    doctor.getFirstName().charAt(0) + "." +
                    (doctor.getMiddleName() != null ? doctor.getMiddleName().charAt(0) + "." : "");
            appointment.setDoctorInitials(doctorInitials);
        }

        if (request.getAppointmentDate() != null) {
            LocalDateTime parsedDate = LocalDateTime.parse(request.getAppointmentDate(),
                    DateTimeFormatter.ISO_DATE_TIME);
            appointment.setAppointmentDate(parsedDate);
        }

        return medicalAppointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) throws Exception {
        if (!medicalAppointmentRepository.existsById(id)) {
            throw new Exception("Appointment not found");
        }
        medicalAppointmentRepository.deleteById(id);
    }

    public MedicalAppointmentResponse convertToResponse(MedicalAppointment appointment) {
        MedicalAppointmentResponse response = new MedicalAppointmentResponse();
        response.setId(appointment.getId());
        response.setAppointmentName(appointment.getAppointmentName());
        response.setAppointmentDate(appointment.getAppointmentDate());
        response.setAppointmentTime(appointment.getAppointmentTime());
        response.setDescription(appointment.getDescription());
        response.setAppointmentType(appointment.getAppointmentType());
        response.setDoctorInitials(appointment.getDoctorInitials());

        response.setStatus(appointment.getStatus().name());
        response.setCategory(appointment.getCategory());
        response.setTitle(appointment.getTitle());
        response.setDuration(appointment.getDuration());
        response.setPrice(appointment.getPrice());
        response.setCompletedAt(appointment.getCompletedAt());
        response.setPatientName(appointment.getPatientName());

        if (appointment.getMedicalCard() != null && appointment.getMedicalCard().getChild() != null) {
            response.setChildId(appointment.getMedicalCard().getChild().getId());
        }

        if (appointment.getDoctor() != null) {
            response.setDoctor(doctorService.convertToResponse(appointment.getDoctor()));
        }

        if (appointment.getService() != null) {
            response.setService(serviceService.convertToResponse(appointment.getService()));
        }

        return response;
    }

    public List<MedicalAppointmentResponse> findAppointmentResponsesByMedicalCardId(Long medicalCardId) {
        return findByMedicalCardId(medicalCardId).stream()
                .map(this::convertToResponse)
                .toList();
    }

    // История консультаций
    public List<MedicalAppointmentResponse> getCompletedConsultationsByPatientId(Long patientId, String sortBy) {
        var appointments = medicalAppointmentRepository.findByPatientIdAndStatus(
                patientId, 
                com.example.medicalapp.entity.AppointmentStatus.COMPLETED
        );

        return sortConsultations(appointments, sortBy).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<MedicalAppointmentResponse> getCompletedConsultationsByPatientIdAndYear(Long patientId, Integer year, String sortBy) {
        var appointments = medicalAppointmentRepository.findCompletedByPatientIdAndYear(patientId, year);

        return sortConsultations(appointments, sortBy).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<MedicalAppointmentResponse> getCompletedConsultationsByChildId(Long childId) {
        return medicalAppointmentRepository.findCompletedByChildId(childId).stream()
                .map(this::convertToResponse)
                .toList();
    }

    private List<MedicalAppointment> sortConsultations(List<MedicalAppointment> appointments, String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "date_desc";
        }

        return switch (sortBy) {
            case "date_asc" -> appointments.stream()
                    .sorted((a, b) -> a.getCompletedAt().compareTo(b.getCompletedAt()))
                    .toList();
            case "category" -> appointments.stream()
                    .sorted((a, b) -> {
                        String catA = a.getCategory() != null ? a.getCategory() : "";
                        String catB = b.getCategory() != null ? b.getCategory() : "";
                        return catA.compareToIgnoreCase(catB);
                    })
                    .toList();
            default -> appointments.stream()
                    .sorted((a, b) -> b.getCompletedAt().compareTo(a.getCompletedAt()))
                    .toList();
        };
    }
}

