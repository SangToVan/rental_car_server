package com.sangto.rental_car_server.domain.dto.user;

import com.sangto.rental_car_server.domain.enums.EUserRole;
import lombok.Builder;

@Builder
public record UserDetailResponseDTO(
        Integer userId,
        String username,
        String email,
        boolean isActive,
        EUserRole role
) {
}
