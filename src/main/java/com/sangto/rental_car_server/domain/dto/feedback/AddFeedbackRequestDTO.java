package com.sangto.rental_car_server.domain.dto.feedback;

import lombok.Builder;

@Builder
public record AddFeedbackRequestDTO(
        Integer rating,
        String content
) {
}
