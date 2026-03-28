package com.opd_care.controller;

import com.opd_care.dto.ApiResponse;
import com.opd_care.dto.PatientHistoryReportDTO;
import com.opd_care.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/patient-history/{patientId}")
    public ResponseEntity<ApiResponse<PatientHistoryReportDTO>> getPatientHistoryReport(
            @PathVariable String patientId) {
        return ResponseEntity.ok(ApiResponse.success(
                reportService.getPatientHistoryReport(patientId),
                "Patient clinical history report generated successfully"));
    }
}
