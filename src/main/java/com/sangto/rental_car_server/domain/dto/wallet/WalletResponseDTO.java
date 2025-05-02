package com.sangto.rental_car_server.domain.dto.wallet;

import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WalletResponseDTO(
        UserResponseDTO info,
        String balance,
        String updatedAt
) {
}
