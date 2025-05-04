package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.notification.AddNotificationRequestDTO;
import com.sangto.rental_car_server.responses.Response;

public interface NotificationService {
    Response<String> addNotification(AddNotificationRequestDTO requestDTO);
}
