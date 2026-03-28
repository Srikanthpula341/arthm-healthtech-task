package com.opd_care.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientHistoryReportDTO {
    private PatientDTO patient;
    private List<VisitDTO> visitHistory;
    private Integer totalVisits;
}
