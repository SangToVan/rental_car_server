package com.sangto.rental_car_server.domain.dto.report;

import com.sangto.rental_car_server.domain.enums.EReportStatus;
import com.sangto.rental_car_server.domain.enums.EReportTargetType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReportResponseDTO(
        Integer reportId,
        Integer reporterId,
        Integer bookingId,
        EReportTargetType targetType,
        Integer targetId,
        String content,
        EReportStatus status,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt,
        String adminNote
) {
}
