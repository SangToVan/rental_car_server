package com.sangto.rental_car_server.domain.dto.auth;

import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;

public record LoginResponseDTO(
        Boolean authenticated,
        String token,
        UserDetailResponseDTO userDetailResponseDTO
) {
}
