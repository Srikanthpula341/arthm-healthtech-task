package com.opd_care.service.impl;

import com.opd_care.constant.AppConstants;
import com.opd_care.constant.ErrorConstants;
import com.opd_care.dto.ConsultationDTO;
import com.opd_care.exception.ResourceNotFoundException;
import com.opd_care.exception.ValidationException;
import com.opd_care.model.Appointment;
import com.opd_care.model.AppointmentStatus;
import com.opd_care.model.Consultation;
import com.opd_care.repository.AppointmentRepository;
import com.opd_care.repository.ConsultationRepository;
import com.opd_care.service.ConsultationService;
import com.opd_care.util.MappingUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConsultationServiceImpl implements ConsultationService {
    private final ConsultationRepository consultationRepository;
    private final AppointmentRepository appointmentRepository;

    public ConsultationServiceImpl(ConsultationRepository consultationRepository,
            AppointmentRepository appointmentRepository) {
        this.consultationRepository = consultationRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public ConsultationDTO completeConsultation(ConsultationDTO dto) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorConstants.APPOINTMENT_NOT_FOUND + dto.getAppointmentId()));

        if (consultationRepository.existsByAppointment(appointment)) {
            throw new ValidationException(ErrorConstants.CONSULTATION_ALREADY_EXISTS);
        }

        Consultation consultation = MappingUtil.toConsultationEntity(dto, appointment);
        consultation.setConsultationId(
                AppConstants.CONSULTATION_ID_PREFIX + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        consultation.setIsCompleted(true);
        consultation.setCompletedAt(LocalDateTime.now());

        Consultation saved = consultationRepository.save(consultation);

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        return MappingUtil.toConsultationDTO(saved);
    }

    @Override
    public List<ConsultationDTO> getPatientHistory(String patientId) {
        return consultationRepository.findByAppointmentPatientPatientIdOrderByCreatedAtDesc(patientId).stream()
                .map(MappingUtil::toConsultationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationDTO> searchConsultations(String patientId, String doctorId, LocalDateTime from,
            LocalDateTime to) {
        // Simple implementation using the combined repository method
        // If from/to are null, we can default to a wide range
        LocalDateTime start = (from != null) ? from : LocalDateTime.now().minusYears(10);
        LocalDateTime end = (to != null) ? to : LocalDateTime.now().plusYears(1);

        if (patientId != null && doctorId != null) {
            return consultationRepository
                    .findByCreatedAtBetweenAndAppointmentPatientPatientIdAndAppointmentDoctorUserId(
                            start, end, patientId, doctorId)
                    .stream()
                    .map(MappingUtil::toConsultationDTO)
                    .collect(Collectors.toList());
        } else {
            // Default to just date range if other params missing (simpler for now)
            return consultationRepository.findByCreatedAtBetween(start, end).stream()
                    .map(MappingUtil::toConsultationDTO)
                    .collect(Collectors.toList());
        }
    }
}
