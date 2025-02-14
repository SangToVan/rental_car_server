package com.sangto.rental_car_server.domain.dto.payment;

import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.EPaymentType;
import com.sangto.rental_car_server.domain.enums.ETransactionStatus;
import lombok.Builder;

import java.util.Date;

@Builder
public record PaymentResponseDTO(
        Integer booking_id,
        Double amount,
        EPaymentType paymentType,
        EPaymentMethod paymentMethod,
        ETransactionStatus status,
        Date transaction_date
) {
}
