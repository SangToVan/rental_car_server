package com.sangto.rental_car_server.domain.dto.booking;

import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BookingResponseDTO(
        Integer bookingId,
        Integer carId,
        String carName,
        BigDecimal basePrice,
        String startDateTime,
        String endDateTime,
        Long numberOfHour,
        String totalPrice,
        String rentalFee,
        EBookingStatus bookingStatus
) {
}
