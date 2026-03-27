package com.opd_care.service;

import com.opd_care.dto.AppointmentDTO;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentDTO bookAppointment(AppointmentDTO dto);

    List<AppointmentDTO> getTodayAppointments();

    List<AppointmentDTO> getAllAppointments();

    List<AppointmentDTO> searchAppointments(LocalDate from, LocalDate to);
}
