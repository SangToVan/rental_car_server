package com.sangto.rental_car_server.domain.dto.user;

import com.sangto.rental_car_server.domain.enums.EUserRole;
import lombok.Builder;

@Builder
public record UserResponseDTO(
        Integer userId,
        String username,
        String email,
        EUserRole role,
        String phoneNumber,
        String avatar,
        boolean isActive,
        String balance
) {
}
