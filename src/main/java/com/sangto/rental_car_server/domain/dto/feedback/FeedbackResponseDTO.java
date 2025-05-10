package com.sangto.rental_car_server.domain.dto.feedback;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FeedbackResponseDTO(
        String avatar,
        String username,
        String content,
        Integer rating,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate createdAt
) {
}
