package com.sangto.rental_car_server.domain.dto.admin;

import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record UserResponseForAdminDTO(
        Integer customerCount,
        Integer ownerCount,
        List<UserResponseDTO> list
) {
}
