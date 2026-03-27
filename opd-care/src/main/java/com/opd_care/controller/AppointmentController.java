package com.opd_care.controller;

import com.opd_care.dto.ApiResponse;
import com.opd_care.dto.AppointmentDTO;
import com.opd_care.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> searchAppointments(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        List<AppointmentDTO> appointments = appointmentService.searchAppointments(from, to);
        if ("DOCTOR".equals(role) && userId != null) {
            appointments = appointments.stream()
                    .filter(a -> userId.equals(a.getDoctorId()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments searched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentDTO>> bookAppointment(@Valid @RequestBody AppointmentDTO dto) {
        return new ResponseEntity<>(ApiResponse.success(appointmentService.bookAppointment(dto), "Appointment booked successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> getTodayAppointments(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        List<AppointmentDTO> appointments = appointmentService.getTodayAppointments();
        if ("DOCTOR".equals(role) && userId != null) {
            appointments = appointments.stream()
                    .filter(a -> userId.equals(a.getDoctorId()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(ApiResponse.success(appointments, "Today's appointments retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> getAllAppointments(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        List<AppointmentDTO> appointments = appointmentService.getAllAppointments();
        if ("DOCTOR".equals(role) && userId != null) {
            appointments = appointments.stream()
                    .filter(a -> userId.equals(a.getDoctorId()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(ApiResponse.success(appointments, "All appointments retrieved successfully"));
    }
}
