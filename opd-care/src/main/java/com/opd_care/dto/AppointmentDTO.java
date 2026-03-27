package com.opd_care.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {
    private String appointmentId;

    @NotBlank(message = "Patient ID is required")
    private String patientId;

    private String patientName;

    @NotNull(message = "Date is required")
    private LocalDate appointmentDate;

    @NotNull(message = "Time is required")
    private LocalTime appointmentTime;

    @NotBlank(message = "Doctor ID is required")
    private String doctorId;

    private String doctorName;

    private String notes;

    private String status;
}
