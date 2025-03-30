package com.sangto.rental_car_server.domain.dto.transaction;

import com.sangto.rental_car_server.domain.enums.ETransactionType;
import lombok.Builder;

@Builder
public record AddTransactionRequestDTO(
        ETransactionType transactionType,
        String amount,
        String description,
        Integer walletId
) {
}
