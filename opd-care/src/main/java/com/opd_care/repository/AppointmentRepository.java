package com.opd_care.repository;

import com.opd_care.model.Appointment;
import com.opd_care.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByAppointmentDate(LocalDate date);
    List<Appointment> findByAppointmentDateBetween(LocalDate startDate, LocalDate endDate);
    List<Appointment> findByPatientPatientId(String patientId);
    boolean existsByPatientAndAppointmentDateAndAppointmentTime(Patient patient, LocalDate date, LocalTime time);
    boolean existsByDoctorAndAppointmentDateAndAppointmentTime(com.opd_care.model.User doctor, LocalDate date, LocalTime time);
}
