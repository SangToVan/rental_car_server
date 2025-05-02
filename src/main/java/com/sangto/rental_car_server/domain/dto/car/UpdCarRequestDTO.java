package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.enums.ECarStatus;

public record UpdCarRequestDTO(
        String name,
        String licensePlate,
        String brand,
        String color,
        String model,
        Integer numberOfSeats,
        Integer productionYear,
        String basePrice
) {
}
