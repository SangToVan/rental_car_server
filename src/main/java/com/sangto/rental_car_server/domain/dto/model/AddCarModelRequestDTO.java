package com.sangto.rental_car_server.domain.dto.model;

import com.sangto.rental_car_server.domain.enums.ECarType;
import lombok.Builder;

@Builder
public record AddCarModelRequestDTO(
        String modelName,
        ECarType carType,
        Integer brandId
) {
}
