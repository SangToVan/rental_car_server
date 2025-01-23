package com.sangto.rental_car_server.domain.dto.payment;

import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.EPaymentType;

import java.util.Date;

public record PaymentResponseDTO(
        Double amount,
        EPaymentMethod paymentMethod,
        EPaymentType paymentType,
        Integer booking_id,
        String status,
        Date transaction_date


) {
}
