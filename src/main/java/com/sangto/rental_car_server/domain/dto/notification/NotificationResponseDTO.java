package com.sangto.rental_car_server.domain.dto.notification;

import com.sangto.rental_car_server.domain.enums.ENotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponseDTO(
        ENotificationType notificationType,
        String content,
        Boolean isRead,
        LocalDateTime createdAt,
        String targetUrl
) {
}
