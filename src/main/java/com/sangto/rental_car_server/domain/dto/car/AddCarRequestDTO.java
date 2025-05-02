package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.enums.ECarTransmission;
import com.sangto.rental_car_server.domain.enums.EFuelType;
import lombok.Builder;

@Builder
public record AddCarRequestDTO(
    String name,
    String licensePlate,
    Integer modelId,
    String color,
    Integer numberOfSeats,
    Integer productionYear,
    ECarTransmission transmission,
    EFuelType fuelType,
    Integer mileage,
    Float fuelConsumption,
    String address,
    String description,
    String additionalFunctions,
    String termOfUse,
    String basePrice,
    String[] images
) {
}
