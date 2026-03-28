package com.opd_care.service.impl;

import com.opd_care.dto.*;
import com.opd_care.exception.ResourceNotFoundException;
import com.opd_care.model.Appointment;
import com.opd_care.model.Consultation;
import com.opd_care.model.Patient;
import com.opd_care.repository.AppointmentRepository;
import com.opd_care.repository.ConsultationRepository;
import com.opd_care.repository.PatientRepository;
import com.opd_care.service.ReportService;
import com.opd_care.util.MappingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final ConsultationRepository consultationRepository;

    @Override
    public PatientHistoryReportDTO getPatientHistoryReport(String patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        // Get all appointments for this patient
        List<Appointment> appointments = appointmentRepository.findByPatientPatientId(patientId);

        // Get all consultations for this patient
        List<Consultation> consultations = consultationRepository
                .findByAppointmentPatientPatientIdOrderByCreatedAtDesc(patientId);

        // Map consultations by appointment ID for quick lookup
        Map<String, Consultation> consultationMap = consultations.stream()
                .collect(Collectors.toMap(c -> c.getAppointment().getAppointmentId(), c -> c));

        List<VisitDTO> visitHistory = new ArrayList<>();

        // Create visits by pairing appointments with consultations
        for (Appointment appt : appointments) {
            Consultation cons = consultationMap.get(appt.getAppointmentId());

            visitHistory.add(VisitDTO.builder()
                    .appointment(MappingUtil.toAppointmentDTO(appt))
                    .consultation(MappingUtil.toConsultationDTO(cons))
                    .build());
        }

        // Sort history by date descending
        visitHistory.sort((v1, v2) -> {
            int dateCmp = v2.getAppointment().getAppointmentDate().compareTo(v1.getAppointment().getAppointmentDate());
            if (dateCmp != 0)
                return dateCmp;
            return v2.getAppointment().getAppointmentTime().compareTo(v1.getAppointment().getAppointmentTime());
        });

        return PatientHistoryReportDTO.builder()
                .patient(MappingUtil.toPatientDTO(patient))
                .visitHistory(visitHistory)
                .totalVisits(visitHistory.size())
                .build();
    }
}
