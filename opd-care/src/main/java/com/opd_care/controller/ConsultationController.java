package com.opd_care.controller;

import com.opd_care.dto.ApiResponse;
import com.opd_care.dto.ConsultationDTO;
import com.opd_care.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/consultations")
@RequiredArgsConstructor
public class ConsultationController {
    private final ConsultationService consultationService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ConsultationDTO>>> searchConsultations(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to) {
        return ResponseEntity.ok(ApiResponse.success(consultationService.searchConsultations(patientId, doctorId, from, to), "Consultations searched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ConsultationDTO>> completeConsultation(@Valid @RequestBody ConsultationDTO dto) {
        return new ResponseEntity<>(ApiResponse.success(consultationService.completeConsultation(dto), "Consultation completed successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<ConsultationDTO>>> getPatientHistory(@PathVariable String patientId) {
        return ResponseEntity.ok(ApiResponse.success(consultationService.getPatientHistory(patientId), "Patient clinical history retrieved successfully"));
    }
}
