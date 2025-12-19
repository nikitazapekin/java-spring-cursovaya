package com.example.medicalapp.service;

import com.example.medicalapp.entity.*;
import com.example.medicalapp.models.MedicalAppointmentRequest;
import com.example.medicalapp.models.MedicalAppointmentResponse;
import com.example.medicalapp.repository.DoctorRepository;
import com.example.medicalapp.repository.MedicalAppointmentRepository;
import com.example.medicalapp.repository.MedicalCardRepository;
import com.example.medicalapp.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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



    public List<MedicalAppointmentResponse> findAnalyzesByMedicalCardId(Long medicalCardId) {
        System.out.println("=== Getting all analyzes for medicalCardId: " + medicalCardId + " ===");

        List<MedicalAppointment> analyzes = medicalAppointmentRepository
                .findByMedicalCardIdAndAppointmentType(medicalCardId, "Анализы");

        System.out.println("Total analyzes found in repository: " + analyzes.size());

        for (MedicalAppointment appointment : analyzes) {
            System.out.println("Analyze ID: " + appointment.getId() +
                    ", Name: " + appointment.getAppointmentName() +
                    ", Date: " + appointment.getAppointmentDate() +
                    ", Type: " + appointment.getAppointmentType());
        }

        return analyzes.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }




    public List<MedicalAppointmentResponse> findAnalyzesByMedicalCardIdAndDate(Long medicalCardId, String dateString) {

        System.out.println("MedicalCardId: " + medicalCardId);
        System.out.println("Date string: '" + dateString + "'");

        LocalDate date = parseDate(dateString);
        System.out.println("Parsed date: " + date);

        List<MedicalAppointment> allAnalyzes = medicalAppointmentRepository
                .findByMedicalCardIdAndAppointmentType(medicalCardId, "Анализы");

        System.out.println("Total analyzes found for medical card: " + allAnalyzes.size());


        List<MedicalAppointment> filteredAnalyzes = allAnalyzes.stream()
                .filter(appointment -> {
                    if (appointment.getAppointmentDate() == null) {
                        System.out.println("  Appointment ID " + appointment.getId() + ": null appointment date");
                        return false;
                    }


                    LocalDate appointmentDate = appointment.getAppointmentDate().toLocalDate();
                    boolean matches = appointmentDate.equals(date);


                    return matches;
                })
                .collect(Collectors.toList());


        return filteredAnalyzes.stream()
                .map(this::convertToResponse)
                .toList();
    }

    private LocalDate parseDate(String dateString) {
        try {
            System.out.println("Parsing date string: '" + dateString + "'");

            try {

                LocalDate date = LocalDate.parse(dateString);
                System.out.println("Parsed as ISO (yyyy-MM-dd): " + date);
                return date;
            } catch (Exception e1) {
                System.out.println("Failed to parse as ISO: " + e1.getMessage());

                try {

                    LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"));

                    return date;
                } catch (Exception e2) {
                    System.out.println("Failed to parse as dd.MM.yyyy: " + e2.getMessage());

                    String[] formats = {
                            "yyyy/MM/dd", "dd/MM/yyyy", "MM/dd/yyyy",
                            "yyyy.MM.dd", "dd.MM.yyyy", "MM.dd.yyyy"
                    };

                    for (String format : formats) {
                        try {
                            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(format));
                            System.out.println("Parsed as " + format + ": " + date);
                            return date;
                        } catch (Exception e3) {

                        }
                    }

                    throw new RuntimeException("Could not parse date: " + dateString);
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR parsing date: " + dateString + " - " + e.getMessage());
            throw new RuntimeException("Invalid date format. Use yyyy-MM-dd or dd.MM.yyyy. Received: " + dateString);
        }
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

        String patientName = appointment.getPatientName();
        if ((patientName == null || patientName.isEmpty()) && 
            appointment.getMedicalCard() != null && 
            appointment.getMedicalCard().getChild() != null) {
            patientName = appointment.getMedicalCard().getChild().getName();
        }
        response.setPatientName(patientName);

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
/*
    public List<MedicalAppointmentResponse> findAnalyzesByMedicalCardId(Long medicalCardId) {
        return medicalAppointmentRepository.findByMedicalCardIdAndAppointmentType(medicalCardId, "Анализы").stream()
                .map(this::convertToResponse)
                .toList();
    }


 */

    public List<MedicalAppointmentResponse> getCompletedConsultationsByPatientId(Long patientId, String sortBy) {
        var appointments = medicalAppointmentRepository.findByPatientIdAndStatus(
                patientId, 
                AppointmentStatus.COMPLETED
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

    public List<MedicalAppointmentResponse> getAllAppointmentsByPatient(
            Long patientId, 
            Integer year, 
            String status,
            String sortBy, 
            String search) {
        
        List<MedicalAppointment> appointments;

        if (year != null && status != null && !status.isEmpty()) {
            appointments = medicalAppointmentRepository.findByPatientIdYearAndStatus(
                patientId, year, AppointmentStatus.valueOf(status));
        } else if (year != null) {
            appointments = medicalAppointmentRepository.findByPatientIdAndYear(patientId, year);
        } else if (status != null && !status.isEmpty()) {
            appointments = medicalAppointmentRepository.findByPatientIdAndStatus(
                patientId, AppointmentStatus.valueOf(status));
        } else {
            appointments = medicalAppointmentRepository.findAllByPatientId(patientId);
        }
        

        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase();
            appointments = appointments.stream()
                    .filter(a -> 
                        (a.getAppointmentName() != null && a.getAppointmentName().toLowerCase().contains(searchLower)) ||
                        (a.getDoctorInitials() != null && a.getDoctorInitials().toLowerCase().contains(searchLower)) ||
                        (a.getCategory() != null && a.getCategory().toLowerCase().contains(searchLower)) ||
                        (a.getService() != null && a.getService().getTitle().toLowerCase().contains(searchLower))
                    )
                    .toList();
        }
        

        appointments = sortAppointments(appointments, sortBy);
        
        return appointments.stream()
                .map(this::convertToResponse)
                .toList();
    }
    
    private List<MedicalAppointment> sortAppointments(List<MedicalAppointment> appointments, String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "date_desc";
        }

        return switch (sortBy) {
            case "date_asc" -> appointments.stream()
                    .sorted((a, b) -> a.getAppointmentDate().compareTo(b.getAppointmentDate()))
                    .toList();
            case "date_desc" -> appointments.stream()
                    .sorted((a, b) -> b.getAppointmentDate().compareTo(a.getAppointmentDate()))
                    .toList();
            case "service" -> appointments.stream()
                    .sorted((a, b) -> {
                        String sA = a.getService() != null ? a.getService().getTitle() : "";
                        String sB = b.getService() != null ? b.getService().getTitle() : "";
                        return sA.compareToIgnoreCase(sB);
                    })
                    .toList();
            case "category" -> appointments.stream()
                    .sorted((a, b) -> {
                        String catA = a.getCategory() != null ? a.getCategory() : "";
                        String catB = b.getCategory() != null ? b.getCategory() : "";
                        return catA.compareToIgnoreCase(catB);
                    })
                    .toList();
            case "doctor" -> appointments.stream()
                    .sorted((a, b) -> {
                        String dA = a.getDoctorInitials() != null ? a.getDoctorInitials() : "";
                        String dB = b.getDoctorInitials() != null ? b.getDoctorInitials() : "";
                        return dA.compareToIgnoreCase(dB);
                    })
                    .toList();
            default -> appointments.stream()
                    .sorted((a, b) -> b.getAppointmentDate().compareTo(a.getAppointmentDate()))
                    .toList();
        };
    }
    

    public MedicalAppointment cancelAppointment(Long id) throws Exception {
        MedicalAppointment appointment = medicalAppointmentRepository.findById(id)
                .orElseThrow(() -> new Exception("Appointment not found"));
        
        appointment.setStatus(AppointmentStatus.CANCELLED);
        
        return medicalAppointmentRepository.save(appointment);
    }
    

    public MedicalAppointment rescheduleAppointment(Long id, LocalDateTime newDate, String newTime) throws Exception {
        MedicalAppointment appointment = medicalAppointmentRepository.findById(id)
                .orElseThrow(() -> new Exception("Appointment not found"));
        
        appointment.setAppointmentDate(newDate);
        if (newTime != null) {
            appointment.setAppointmentTime(newTime);
        }
        
        return medicalAppointmentRepository.save(appointment);
    }
    

    public List<MedicalAppointmentResponse> getTodayAppointmentsByDoctorId(Long doctorId) {
        return medicalAppointmentRepository.findTodayAppointmentsByDoctorId(doctorId).stream()
                .map(this::convertToResponse)
                .toList();
    }
    

    public List<MedicalAppointmentResponse> getAllAppointmentsByDoctorId(Long doctorId) {
        return medicalAppointmentRepository.findAllByDoctorId(doctorId).stream()
                .map(this::convertToResponse)
                .toList();
    }
}

