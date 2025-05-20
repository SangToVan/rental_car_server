package com.sangto.rental_car_server.domain.dto.admin;

import com.sangto.rental_car_server.domain.dto.escrow_transaction.EscrowTransactionResponseDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record EscrowResponseForAdminDTO(
        String totalEscrow,
        List<EscrowTransactionResponseDTO> list
) {
}
