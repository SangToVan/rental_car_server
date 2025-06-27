package com.sangto.rental_car_server.domain.dto.report;

import lombok.Builder;

@Builder
public record AddReportRequestDTO(
        String content,
        String[] images
) {
}
