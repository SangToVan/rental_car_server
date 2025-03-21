package com.sangto.rental_car_server.domain.dto.booking;

import com.sangto.rental_car_server.domain.dto.car.CarDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import lombok.Builder;

@Builder
public record BookingDetailResponseDTO(
        Integer bookingId,
        CarDetailResponseDTO carDetail,
        UserDetailResponseDTO customerInfo,
        String startDateTime,
        String endDateTime,
        EPaymentMethod paymentMethod,
        EBookingStatus status,
        Double totalPrice,
        Long numberOfHour
) {
}
