package com.opd_care.dto;

import com.opd_care.constant.ErrorConstants;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationDTO {
    private String consultationId;

    @NotBlank(message = "Appointment ID is required")
    private String appointmentId;

    private String patientName;

    @NotNull(message = "Systolic BP is required")
    @Min(60) @Max(200)
    private Integer systolicBP;

    @NotNull(message = "Diastolic BP is required")
    @Min(40) @Max(130)
    private Integer diastolicBP;

    @NotNull(message = "Temperature is required")
    @DecimalMin(value = "95.0", message = ErrorConstants.INVALID_TEMP)
    @DecimalMax(value = "108.0", message = ErrorConstants.INVALID_TEMP)
    private BigDecimal temperature;

    @NotBlank(message = "Notes are required")
    @Size(min = 10, max = 500)
    private String notes;

    private Boolean isCompleted;

    private LocalDateTime completedAt;
}
