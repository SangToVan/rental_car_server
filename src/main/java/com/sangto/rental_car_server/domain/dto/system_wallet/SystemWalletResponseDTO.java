package com.sangto.rental_car_server.domain.dto.system_wallet;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SystemWalletResponseDTO(
        String service_fee,
        String rental_fee,
        String reserve,
        String total,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
