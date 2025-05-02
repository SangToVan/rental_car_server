package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.enums.ECarStatus;
import lombok.Builder;

@Builder
public record CarDetailResponseDTO(
        Integer id,
        String name,
        String licensePlate,
        String brand,
        String color,
        String model,
        Integer numberOfSeats,
        Integer productionYear,
        String basePrice,
        ECarStatus carStatus
) {
}
