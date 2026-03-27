package com.opd_care.controller;

import com.opd_care.dto.ApiResponse;
import com.opd_care.dto.PatientDTO;
import com.opd_care.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<ApiResponse<PatientDTO>> registerPatient(@Valid @RequestBody PatientDTO dto) {
        return new ResponseEntity<>(ApiResponse.success(patientService.registerPatient(dto), "Patient registered successfully"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientDTO>>> getAllPatients() {
        return ResponseEntity.ok(ApiResponse.success(patientService.getAllPatients(), "Patients retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientDTO>> getPatientById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(patientService.getPatientById(id), "Patient retrieved successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PatientDTO>>> searchPatients(@RequestParam String query) {
        return ResponseEntity.ok(ApiResponse.success(patientService.searchPatients(query), "Patients searched successfully"));
    }
}
