package com.opd_care.service.impl;

import com.opd_care.constant.AppConstants;
import com.opd_care.constant.ErrorConstants;
import com.opd_care.dto.PatientDTO;
import com.opd_care.exception.ResourceNotFoundException;
import com.opd_care.exception.ValidationException;
import com.opd_care.model.Patient;
import com.opd_care.repository.PatientRepository;
import com.opd_care.service.PatientService;
import com.opd_care.util.MappingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientDTO registerPatient(PatientDTO dto) {
        if (patientRepository.existsByPhone(dto.getPhone())) {
            throw new ValidationException(ErrorConstants.PHONE_EXISTS);
        }

        Patient patient = MappingUtil.toPatientEntity(dto);
        patient.setPatientId(AppConstants.PATIENT_ID_PREFIX + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        Patient saved = patientRepository.save(patient);
        return MappingUtil.toPatientDTO(saved);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(MappingUtil::toPatientDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDTO getPatientById(String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.PATIENT_NOT_FOUND + id));
        return MappingUtil.toPatientDTO(patient);
    }

    @Override
    public List<PatientDTO> searchPatients(String query) {
        return patientRepository.findByNameContainingIgnoreCase(query).stream()
                .map(MappingUtil::toPatientDTO)
                .collect(Collectors.toList());
    }
}
