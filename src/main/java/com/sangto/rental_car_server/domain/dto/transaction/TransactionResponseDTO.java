package com.sangto.rental_car_server.domain.dto.transaction;

import com.sangto.rental_car_server.domain.enums.ETransactionType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionResponseDTO(
        ETransactionType transactionType,
        String amount,
        String description,
        Integer walletId,
        String transactionDate
) {
}
