package com.sangto.rental_car_server.domain.dto.admin;

import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record BookingResponseForAdminDTO(
        Integer finishedBooking,
        Integer unfinishedBooking,
        List<BookingResponseDTO> list
) {
}
