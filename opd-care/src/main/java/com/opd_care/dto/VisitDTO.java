package com.opd_care.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitDTO {
    private AppointmentDTO appointment;
    private ConsultationDTO consultation; // Optional
}
