package com.sangto.rental_car_server.domain.dto.admin;

import com.sangto.rental_car_server.domain.dto.car.CarResponseDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record CarResponseForAdminDTO(
        Integer verifiedCarCount,
        Integer unverifiedCarCount,
        Integer waitingCarCount,
        List<CarResponseDTO> list
) {
}
