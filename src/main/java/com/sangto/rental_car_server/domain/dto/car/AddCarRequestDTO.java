package com.sangto.rental_car_server.domain.dto.car;

import lombok.Builder;

@Builder
public record AddCarRequestDTO(
    String name,
    String licensePlate,
    String brand,
    String color,
    String model,
    Integer numberOfSeats,
    Integer productionYear
) {
}
