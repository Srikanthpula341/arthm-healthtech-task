package com.opd_care.service;

import com.opd_care.constant.ErrorConstants;
import com.opd_care.dto.PatientDTO;
import com.opd_care.exception.ValidationException;
import com.opd_care.model.Patient;
import com.opd_care.repository.PatientRepository;
import com.opd_care.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    private PatientService patientService;

    @BeforeEach
    void setUp() {
        patientService = new PatientServiceImpl(patientRepository);
    }

    @Test
    void registerPatient_NewPhone_ReturnsDto() {
        // Arrange
        PatientDTO dto = PatientDTO.builder()
                .name("John Doe")
                .gender("M")
                .age(30)
                .phone("1234567890")
                .build();

        when(patientRepository.existsByPhone(anyString())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        PatientDTO result = patientService.registerPatient(dto);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void registerPatient_DuplicatePhone_ThrowsException() {
        // Arrange
        PatientDTO dto = PatientDTO.builder()
                .phone("1234567890")
                .build();

        when(patientRepository.existsByPhone("1234567890")).thenReturn(true);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> 
            patientService.registerPatient(dto)
        );
        assertEquals(ErrorConstants.PHONE_EXISTS, exception.getMessage());
    }

    @Test
    void getPatientById_Exists_ReturnsDto() {
        // Arrange
        String id = "P-123";
        Patient patient = Patient.builder()
                .patientId(id)
                .name("Jane Doe")
                .build();

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        // Act
        PatientDTO result = patientService.getPatientById(id);

        // Assert
        assertEquals("Jane Doe", result.getName());
    }
}
