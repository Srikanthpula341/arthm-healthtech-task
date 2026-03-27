package com.opd_care.service;

import com.opd_care.dto.PatientDTO;
import java.util.List;

public interface PatientService {
    PatientDTO registerPatient(PatientDTO dto);
    List<PatientDTO> getAllPatients();
    PatientDTO getPatientById(String id);
    List<PatientDTO> searchPatients(String query);
}
