package com.sangto.rental_car_server.domain.dto.payment;

import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.EPaymentType;
import lombok.Builder;

@Builder
public record AddPaymentRequestDTO(
        EPaymentMethod paymentMethod,
        EPaymentType paymentType
) {
}
