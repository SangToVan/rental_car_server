package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.brand.AddCarBrandRequestDTO;
import com.sangto.rental_car_server.domain.entity.CarBrand;
import com.sangto.rental_car_server.domain.mapper.CarBrandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarBrandMapperImpl implements CarBrandMapper {
    @Override
    public CarBrand addCarBrandRequestDTOtoEntity(AddCarBrandRequestDTO requestDTO) {
        return CarBrand.builder()
                .name(requestDTO.brandName())
                .build();
    }
}
