package com.opd_care.service.impl;

import com.opd_care.constant.AppConstants;
import com.opd_care.constant.ErrorConstants;
import com.opd_care.dto.AppointmentDTO;
import com.opd_care.exception.ResourceNotFoundException;
import com.opd_care.exception.ValidationException;
import com.opd_care.model.Appointment;
import com.opd_care.exception.ValidationException;
import com.opd_care.model.Appointment;
import com.opd_care.model.Patient;
import com.opd_care.repository.AppointmentRepository;
import com.opd_care.repository.PatientRepository;
import com.opd_care.repository.UserRepository;
import com.opd_care.service.AppointmentService;
import com.opd_care.util.MappingUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AppointmentDTO bookAppointment(AppointmentDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(ErrorConstants.PATIENT_NOT_FOUND + dto.getPatientId()));

        com.opd_care.model.User doctor = userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + dto.getDoctorId()));

        // Perform Validations
        validateDoctor(doctor);
        validateSchedule(dto);
        checkAvailability(patient, doctor, dto);

        Appointment appointment = MappingUtil.toAppointmentEntity(dto, patient, doctor);
        appointment.setAppointmentId(
                AppConstants.APPOINTMENT_ID_PREFIX + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        appointment.setStatus(com.opd_care.model.AppointmentStatus.PENDING);

        Appointment saved = appointmentRepository.save(appointment);
        return MappingUtil.toAppointmentDTO(saved);
    }

    private void validateDoctor(com.opd_care.model.User doctor) {
        if (doctor.getRole() != com.opd_care.model.UserRole.DOCTOR) {
            throw new ValidationException("Selected user is not a doctor");
        }
    }

    private void validateSchedule(AppointmentDTO dto) {
        if (dto.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new ValidationException(ErrorConstants.PAST_DATE_BOOKING);
        }

        if (dto.getAppointmentTime().isBefore(LocalTime.of(AppConstants.START_HOUR, 0)) ||
                dto.getAppointmentTime().isAfter(LocalTime.of(AppConstants.END_HOUR, 0))) {
            throw new ValidationException(ErrorConstants.INVALID_TIME);
        }

        if (dto.getAppointmentTime().getMinute() != 0) {
            throw new ValidationException(
                    "Appointments must be booked on the hour (e.g. 10:00). 1-hour slots enforced.");
        }
    }

    private void checkAvailability(Patient patient, com.opd_care.model.User doctor, AppointmentDTO dto) {
        if (appointmentRepository.existsByPatientAndAppointmentDateAndAppointmentTime(patient, dto.getAppointmentDate(),
                dto.getAppointmentTime())) {
            throw new ValidationException(ErrorConstants.DOUBLE_BOOKING);
        }

        if (appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, dto.getAppointmentDate(),
                dto.getAppointmentTime())) {
            throw new ValidationException(ErrorConstants.DOCTOR_DOUBLE_BOOKING);
        }
    }

    @Override
    public List<AppointmentDTO> getTodayAppointments() {
        return appointmentRepository.findByAppointmentDate(LocalDate.now()).stream()
                .map(MappingUtil::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(MappingUtil::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> searchAppointments(LocalDate from, LocalDate to) {
        if (from == null && to == null) {
            return getAllAppointments();
        }
        if (from != null && to == null) {
            return appointmentRepository.findByAppointmentDate(from).stream()
                    .map(MappingUtil::toAppointmentDTO)
                    .collect(Collectors.toList());
        }
        return appointmentRepository.findByAppointmentDateBetween(from, to).stream()
                .map(MappingUtil::toAppointmentDTO)
                .collect(Collectors.toList());
    }
}
