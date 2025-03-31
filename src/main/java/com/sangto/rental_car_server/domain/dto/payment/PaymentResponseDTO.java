package com.sangto.rental_car_server.domain.dto.payment;

import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.EPaymentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentResponseDTO(
        String amount,
        EPaymentMethod paymentMethod,
        EPaymentStatus paymentStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer bookingId
) {
}
