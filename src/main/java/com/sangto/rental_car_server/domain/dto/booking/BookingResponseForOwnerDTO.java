package com.sangto.rental_car_server.domain.dto.booking;

import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import lombok.Builder;

@Builder
public record BookingResponseForOwnerDTO(
        Integer bookingId,
        UserDetailResponseDTO customerInfo,
        String startDateTime,
        String endDateTime,
        EPaymentMethod paymentMethod,
        EBookingStatus status
) {
}
