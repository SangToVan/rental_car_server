package com.sangto.rental_car_server.domain.dto.transaction;

import com.sangto.rental_car_server.domain.enums.EPaymentStatus;
import com.sangto.rental_car_server.domain.enums.ETransactionType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AddTransactionRequestDTO(
        ETransactionType transactionType,
        BigDecimal amount,
        String description,
        EPaymentStatus status,
        Integer walletId
) {
}
