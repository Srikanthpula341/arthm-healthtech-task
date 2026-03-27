package com.opd_care.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDTO {
    private String patientId;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotNull(message = "Age is required")
    @Min(1) @Max(120)
    private Integer age;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$")
    private String phone;
}
