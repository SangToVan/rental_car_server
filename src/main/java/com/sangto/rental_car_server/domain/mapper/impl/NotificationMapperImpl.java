package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.notification.AddNotificationRequestDTO;
import com.sangto.rental_car_server.domain.dto.notification.NotificationResponseDTO;
import com.sangto.rental_car_server.domain.entity.Notification;
import com.sangto.rental_car_server.domain.mapper.NotificationMapper;
import com.sangto.rental_car_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMapperImpl implements NotificationMapper {

    private final UserRepository userRepo;

    @Override
    public Notification addNotificationRequestDTOtoEntity(AddNotificationRequestDTO requestDTO) {
        return Notification.builder()
                .user(userRepo.findById(requestDTO.userId()).orElse(null))
                .notificationType(requestDTO.notificationType())
                .content(requestDTO.content())
                .targetUrl(requestDTO.targetUrl())
                .build();
    }

    @Override
    public NotificationResponseDTO toNotificationResponseDTO(Notification entity) {

        return NotificationResponseDTO.builder()
                .notificationType(entity.getNotificationType())
                .content(entity.getContent())
                .isRead(entity.isRead())
                .createdAt(entity.getCreatedAt())
                .targetUrl(entity.getTargetUrl())
                .build();
    }
}
