package com.sangto.rental_car_server.domain.dto.notification;

import com.sangto.rental_car_server.domain.enums.ENotificationType;
import lombok.Builder;

@Builder
public record AddNotificationRequestDTO(
        Integer userId,
        ENotificationType notificationType,
        String content,
        String targetUrl
) {
}
