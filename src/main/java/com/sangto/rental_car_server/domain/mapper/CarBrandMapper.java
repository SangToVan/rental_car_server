package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.brand.AddCarBrandRequestDTO;
import com.sangto.rental_car_server.domain.entity.CarBrand;

public interface CarBrandMapper {
    CarBrand addCarBrandRequestDTOtoEntity(AddCarBrandRequestDTO requestDTO);
}
