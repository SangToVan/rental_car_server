package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.UpdImageRequestDTO;

public record UpdCarRequestDTO(
        Integer mileage,
        Float fuelConsumption,
        String address,
        String description,
        String additionalFunctions,
        String termOfUse,
        String basePrice,
        UpdImageRequestDTO[] images
) {
}
