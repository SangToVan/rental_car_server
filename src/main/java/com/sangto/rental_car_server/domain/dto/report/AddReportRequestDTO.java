package com.sangto.rental_car_server.domain.dto.report;

import com.sangto.rental_car_server.domain.enums.EReportTargetType;
import lombok.Builder;

@Builder
public record AddReportRequestDTO(
        Integer reporterId,
        Integer bookingId,
        EReportTargetType targetType,
        Integer targetId,
        String content
) {
}
