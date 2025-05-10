package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.feedback.AddFeedbackRequestDTO;
import com.sangto.rental_car_server.domain.dto.feedback.FeedbackResponseDTO;
import com.sangto.rental_car_server.domain.entity.Feedback;

public interface FeedbackMapper {
    FeedbackResponseDTO toFeedbackResponseDTO(Feedback entity);

    Feedback addFeedbackRequestToEntity(AddFeedbackRequestDTO requestDTO);
}
