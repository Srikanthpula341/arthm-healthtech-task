package com.opd_care.model;

import com.opd_care.constant.ErrorConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {
    @Id
    private String consultationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @NotNull(message = ErrorConstants.INVALID_BP)
    @Min(value = 60, message = ErrorConstants.INVALID_BP)
    @Max(value = 200, message = ErrorConstants.INVALID_BP)
    private Integer systolicBP;

    @NotNull(message = ErrorConstants.INVALID_BP)
    @Min(value = 40, message = ErrorConstants.INVALID_BP)
    @Max(value = 130, message = ErrorConstants.INVALID_BP)
    private Integer diastolicBP;

    @NotNull(message = ErrorConstants.INVALID_TEMP)
    @DecimalMin(value = "95.0", message = ErrorConstants.INVALID_TEMP)
    @DecimalMax(value = "108.0", message = ErrorConstants.INVALID_TEMP)
    private BigDecimal temperature;

    @NotBlank(message = ErrorConstants.INVALID_NOTES)
    @Size(min = 10, max = 500, message = ErrorConstants.INVALID_NOTES)
    private String notes;

    private Boolean isCompleted;

    private LocalDateTime completedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
