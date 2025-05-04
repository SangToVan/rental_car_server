package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.notification.AddNotificationRequestDTO;
import com.sangto.rental_car_server.domain.dto.notification.NotificationResponseDTO;
import com.sangto.rental_car_server.domain.entity.Notification;

public interface NotificationMapper {
    Notification addNotificationRequestDTOtoEntity(AddNotificationRequestDTO requestDTO);

    NotificationResponseDTO toNotificationResponseDTO(Notification entity);
}
