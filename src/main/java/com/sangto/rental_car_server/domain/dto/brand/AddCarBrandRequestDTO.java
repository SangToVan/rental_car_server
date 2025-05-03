package com.sangto.rental_car_server.domain.dto.brand;

import lombok.Builder;

@Builder
public record AddCarBrandRequestDTO(
        String brandName
) {
}
