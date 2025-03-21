package com.sangto.rental_car_server.domain.dto.booking;

import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import lombok.Builder;

@Builder
public record BookingResponseDTO(
        Integer bookingId,
        Integer carId,
        String carName,
        Double basePrice,
        String startDateTime,
        String endDateTime,
        Double totalPrice,
        EBookingStatus bookingStatus
) {
}
