package com.opd_care.service;

import com.opd_care.constant.AppConstants;
import com.opd_care.constant.ErrorConstants;
import com.opd_care.dto.AppointmentDTO;
import com.opd_care.exception.ResourceNotFoundException;
import com.opd_care.exception.ValidationException;
import com.opd_care.model.Patient;
import com.opd_care.model.User;
import com.opd_care.model.UserRole;
import com.opd_care.repository.AppointmentRepository;
import com.opd_care.repository.PatientRepository;
import com.opd_care.repository.UserRepository;
import com.opd_care.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private UserRepository userRepository;

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentServiceImpl(appointmentRepository, patientRepository, userRepository);
    }

    @Test
    void bookAppointment_ValidData_ReturnsDto() {
        // Arrange
        String pId = "P1";
        String dId = "U1";
        AppointmentDTO dto = AppointmentDTO.builder()
                .patientId(pId)
                .doctorId(dId)
                .appointmentDate(LocalDate.now().plusDays(1))
                .appointmentTime(LocalTime.of(10, 0))
                .build();

        Patient patient = Patient.builder().patientId(pId).build();
        User doctor = User.builder().userId(dId).role(UserRole.DOCTOR).build();

        when(patientRepository.findById(pId)).thenReturn(Optional.of(patient));
        when(userRepository.findById(dId)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        AppointmentDTO result = appointmentService.bookAppointment(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dId, result.getDoctorId());
    }

    @Test
    void bookAppointment_InvalidTimeSlot_ThrowsException() {
        // Arrange
        AppointmentDTO dto = AppointmentDTO.builder()
                .patientId("P1")
                .doctorId("U1")
                .appointmentDate(LocalDate.now().plusDays(1))
                .appointmentTime(LocalTime.of(10, 15)) // Not 00 or 30
                .build();

        Patient patient = Patient.builder().build();
        User doctor = User.builder().role(UserRole.DOCTOR).build();

        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(userRepository.findById(any())).thenReturn(Optional.of(doctor));

        // Act & Assert
        ValidationException ex = assertThrows(ValidationException.class, () -> 
            appointmentService.bookAppointment(dto)
        );
        assertEquals(ErrorConstants.INVALID_TIME, ex.getMessage());
    }

    @Test
    void bookAppointment_SelectedUserNotDoctor_ThrowsException() {
        // Arrange
        AppointmentDTO dto = AppointmentDTO.builder()
                .patientId("P1")
                .doctorId("U1")
                .appointmentDate(LocalDate.now().plusDays(1))
                .appointmentTime(LocalTime.of(10, 0))
                .build();

        Patient patient = Patient.builder().build();
        User admin = User.builder().role(UserRole.ADMIN).build();

        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(userRepository.findById(any())).thenReturn(Optional.of(admin));

        // Act & Assert
        ValidationException ex = assertThrows(ValidationException.class, () -> 
            appointmentService.bookAppointment(dto)
        );
        assertTrue(ex.getMessage().contains("Selected user is not a doctor"));
    }
}
