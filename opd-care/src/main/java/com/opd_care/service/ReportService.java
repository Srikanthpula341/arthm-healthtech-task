package com.opd_care.service;

import com.opd_care.dto.PatientHistoryReportDTO;

public interface ReportService {
    PatientHistoryReportDTO getPatientHistoryReport(String patientId);
}
