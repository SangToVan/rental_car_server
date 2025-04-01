package com.sangto.rental_car_server.domain.dto.escrow_transaction;

import com.sangto.rental_car_server.domain.enums.EscrowStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AddEscrowTransactionRequestDTO(
        Integer bookingId,
        BigDecimal amount,
        EscrowStatus status
) {
}
