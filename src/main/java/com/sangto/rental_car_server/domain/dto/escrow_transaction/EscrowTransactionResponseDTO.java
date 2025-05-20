package com.sangto.rental_car_server.domain.dto.escrow_transaction;

import com.sangto.rental_car_server.domain.enums.EscrowStatus;
import lombok.Builder;

@Builder
public record EscrowTransactionResponseDTO(
        Integer bookingId,
        String amount,
        EscrowStatus status,
        String createdAt,
        String updatedAt
) {
}
