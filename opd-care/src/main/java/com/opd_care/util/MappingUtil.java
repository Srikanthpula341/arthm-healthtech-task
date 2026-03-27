package com.opd_care.util;

import com.opd_care.dto.AppointmentDTO;
import com.opd_care.dto.ConsultationDTO;
import com.opd_care.dto.PatientDTO;
import com.opd_care.dto.UserDTO;
import com.opd_care.model.Appointment;
import com.opd_care.model.Consultation;
import com.opd_care.model.Patient;
import com.opd_care.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MappingUtil {

    // Patient Mapping
    public static PatientDTO toPatientDTO(Patient patient) {
        if (patient == null) return null;
        return PatientDTO.builder()
                .patientId(patient.getPatientId())
                .name(patient.getName())
                .gender(patient.getGender())
                .age(patient.getAge())
                .phone(patient.getPhone())
                .build();
    }

    public static Patient toPatientEntity(PatientDTO dto) {
        if (dto == null) return null;
        return Patient.builder()
                .patientId(dto.getPatientId())
                .name(dto.getName())
                .gender(dto.getGender())
                .age(dto.getAge())
                .phone(dto.getPhone())
                .build();
    }

    // Appointment Mapping
    public static AppointmentDTO toAppointmentDTO(Appointment appointment) {
        if (appointment == null) return null;
        return AppointmentDTO.builder()
                .appointmentId(appointment.getAppointmentId())
                .patientId(appointment.getPatient().getPatientId())
                .patientName(appointment.getPatient().getName())
                .doctorId(appointment.getDoctor().getUserId())
                .doctorName(appointment.getDoctor().getFullName())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .notes(appointment.getNotes())
                .status(appointment.getStatus().name())
                .build();
    }

    public static Appointment toAppointmentEntity(AppointmentDTO dto, Patient patient, User doctor) {
        if (dto == null) return null;
        return Appointment.builder()
                .appointmentId(dto.getAppointmentId())
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(dto.getAppointmentDate())
                .appointmentTime(dto.getAppointmentTime())
                .notes(dto.getNotes())
                .status(dto.getStatus() != null ? com.opd_care.model.AppointmentStatus.valueOf(dto.getStatus()) : com.opd_care.model.AppointmentStatus.PENDING)
                .build();
    }

    // Consultation Mapping
    public static ConsultationDTO toConsultationDTO(Consultation consultation) {
        if (consultation == null) return null;
        return ConsultationDTO.builder()
                .consultationId(consultation.getConsultationId())
                .appointmentId(consultation.getAppointment().getAppointmentId())
                .patientName(consultation.getAppointment().getPatient().getName())
                .systolicBP(consultation.getSystolicBP())
                .diastolicBP(consultation.getDiastolicBP())
                .temperature(consultation.getTemperature())
                .notes(consultation.getNotes())
                .isCompleted(consultation.getIsCompleted())
                .completedAt(consultation.getCompletedAt())
                .build();
    }

    public static Consultation toConsultationEntity(ConsultationDTO dto, Appointment appointment) {
        if (dto == null) return null;
        return Consultation.builder()
                .consultationId(dto.getConsultationId())
                .appointment(appointment)
                .systolicBP(dto.getSystolicBP())
                .diastolicBP(dto.getDiastolicBP())
                .temperature(dto.getTemperature())
                .notes(dto.getNotes())
                .isCompleted(dto.getIsCompleted() != null ? dto.getIsCompleted() : false)
                .completedAt(dto.getCompletedAt())
                .build();
    }

    // User Mapping
    public static UserDTO toUserDTO(User user) {
        if (user == null) return null;
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .build();
    }

    public static User toUserEntity(UserDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .fullName(dto.getFullName())
                .password(dto.getPassword())
                .role(com.opd_care.model.UserRole.valueOf(dto.getRole()))
                .build();
    }
}
