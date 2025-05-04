package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.notification.AddNotificationRequestDTO;
import com.sangto.rental_car_server.domain.entity.Notification;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.mapper.NotificationMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.NotificationRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepo;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepo;

    @Override
    public Response<String> addNotification(AddNotificationRequestDTO requestDTO) {
        Optional<User> findUser = userRepo.findById(requestDTO.userId());
        if (findUser.isEmpty()) throw new AppException("User not found");

        Notification newNotification = notificationMapper.addNotificationRequestDTOtoEntity(requestDTO);
        notificationRepo.save(newNotification);

        return Response.successfulResponse("Added notification successfully");
    }
}
