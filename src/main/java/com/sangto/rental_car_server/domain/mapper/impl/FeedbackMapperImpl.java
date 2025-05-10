package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.feedback.AddFeedbackRequestDTO;
import com.sangto.rental_car_server.domain.dto.feedback.FeedbackResponseDTO;
import com.sangto.rental_car_server.domain.entity.Feedback;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.mapper.FeedbackMapper;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapperImpl implements FeedbackMapper {
    @Override
    public FeedbackResponseDTO toFeedbackResponseDTO(Feedback entity) {

        User customer = entity.getBooking().getUser();

        return FeedbackResponseDTO.builder()
                .avatar(customer.getAvatar())
                .username(customer.getUsername())
                .content(entity.getContent())
                .rating(entity.getRating())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    @Override
    public Feedback addFeedbackRequestToEntity(AddFeedbackRequestDTO requestDTO) {
        return Feedback.builder()
                .rating(requestDTO.rating())
                .content(requestDTO.content())
                .build();
    }
}
