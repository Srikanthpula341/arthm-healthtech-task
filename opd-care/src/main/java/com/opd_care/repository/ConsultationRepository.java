package com.opd_care.repository;

import com.opd_care.model.Appointment;
import com.opd_care.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, String> {
    List<Consultation> findByAppointmentPatientPatientIdOrderByCreatedAtDesc(String patientId);
    
    List<Consultation> findByCreatedAtBetweenAndAppointmentPatientPatientIdAndAppointmentDoctorUserId(
            java.time.LocalDateTime start, java.time.LocalDateTime end, String patientId, String doctorId);

    List<Consultation> findByCreatedAtBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);

    boolean existsByAppointment(Appointment appointment);
}
