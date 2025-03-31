package com.sangto.rental_car_server.domain.dto.payment;

import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.EPaymentStatus;
import lombok.Builder;

@Builder
public record AddPaymentRequestDTO(
        String amount,
        EPaymentMethod paymentMethod,
        EPaymentStatus paymentStatus,
        Integer bookingId,
        Integer userId
) {
}
