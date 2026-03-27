package com.opd_care.service;

import com.opd_care.dto.ConsultationDTO;
import java.util.List;

public interface ConsultationService {
    ConsultationDTO completeConsultation(ConsultationDTO dto);
    List<ConsultationDTO> getPatientHistory(String patientId);
    List<ConsultationDTO> searchConsultations(String patientId, String doctorId, java.time.LocalDateTime from, java.time.LocalDateTime to);
}
