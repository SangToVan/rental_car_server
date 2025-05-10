package com.sangto.rental_car_server.domain.dto.payment;

import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.EPaymentStatus;
import com.sangto.rental_car_server.domain.enums.EPaymentType;
import lombok.Builder;

@Builder
public record PaymentResponseDTO(
        Integer paymentId,
        String amount,
        EPaymentMethod paymentMethod,
        EPaymentStatus paymentStatus,
        EPaymentType paymentType,
        String transactionCode,
        String paymentUrl,
        String createdAt,
        String updatedAt,
        Integer bookingId
) {
}
