package com.sangto.rental_car_server.domain.dto.wallet_transaction;

import com.sangto.rental_car_server.domain.enums.ETransactionType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WalletTransactionResponseDTO(
        String amount,
        ETransactionType transactionType,
        String description,
        LocalDateTime createdAt
) {
}
