package com.sangto.rental_car_server.domain.dto.wallet;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WalletResponseDTO(
        String balance,
        LocalDateTime updatedAt
) {
}
