package com.sangto.rental_car_server.domain.dto.car;

public record UpdCarInfoRequestDTO(
        // Information (Can change)
        Integer mileage,
        Float fuelConsumption,
        String address,
        String description,
        String additionalFunctions,
        String termOfUse
) {
}
