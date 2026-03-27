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

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> searchAppointments(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.searchAppointments(from, to), "Appointments searched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentDTO>> bookAppointment(@Valid @RequestBody AppointmentDTO dto) {
        return new ResponseEntity<>(ApiResponse.success(appointmentService.bookAppointment(dto), "Appointment booked successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> getTodayAppointments() {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getTodayAppointments(), "Today's appointments retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> getAllAppointments() {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getAllAppointments(), "All appointments retrieved successfully"));
    }
}
