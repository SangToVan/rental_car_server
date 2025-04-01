package com.sangto.rental_car_server.domain.dto.escrow_transaction;

import com.sangto.rental_car_server.domain.enums.EscrowStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EscrowTransactionResponseDTO(
        Integer bookingId,
        String amount,
        EscrowStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
