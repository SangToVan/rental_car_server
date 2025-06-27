package com.sangto.rental_car_server.domain.dto.report;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.enums.EReportStatus;
import com.sangto.rental_car_server.domain.enums.EReporter;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReportDetailResponseDTO(
        Integer reportId,
        EReporter reporter,
        String content,
        List<ImageResponseDTO> images,
        EReportStatus status,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt,
        String adminNote
) {
}
