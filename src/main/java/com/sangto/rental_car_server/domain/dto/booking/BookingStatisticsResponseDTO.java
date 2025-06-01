package com.sangto.rental_car_server.domain.dto.booking;

import lombok.Builder;

import java.util.List;

@Builder
public record BookingStatisticsResponseDTO(
        String revenue,
        Integer countCancelled,
        Integer countCompleted,
        List<BookingResponseForOwnerDTO> list
) {
}
