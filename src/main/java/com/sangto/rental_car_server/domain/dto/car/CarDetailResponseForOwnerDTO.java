package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import lombok.Builder;

@Builder
public record CarDetailResponseForOwnerDTO(
        Integer id,
        String name,
        String licensePlate,
        String brand,
        String color,
        String model,
        Integer numberOfSeats,
        Integer productionYear,
        ECarStatus carStatus,
        UserDetailResponseDTO owner
) {
}
