package com.opd_care.model;

import com.opd_care.constant.AppConstants;
import com.opd_care.constant.ErrorConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    private String patientId;

    @NotBlank(message = ErrorConstants.INVALID_NAME)
    @Size(min = 3, max = 50, message = ErrorConstants.INVALID_NAME)
    private String name;

    @NotBlank(message = ErrorConstants.INVALID_GENDER)
    private String gender; // M, F, Other

    @NotNull(message = ErrorConstants.INVALID_AGE)
    @Min(value = 1, message = ErrorConstants.INVALID_AGE)
    @Max(value = 120, message = ErrorConstants.INVALID_AGE)
    private Integer age;

    @NotBlank(message = ErrorConstants.INVALID_PHONE)
    @Pattern(regexp = AppConstants.PHONE_REGEX, message = ErrorConstants.INVALID_PHONE)
    @Column(unique = true)
    private String phone;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
