package com.sangto.rental_car_server.domain.dto.report;

import com.sangto.rental_car_server.domain.enums.EReportStatus;
import lombok.Builder;

@Builder
public record UpdReportRequestDTO(
        Integer reportId,
        EReportStatus status,
        String adminNote
) {
}
