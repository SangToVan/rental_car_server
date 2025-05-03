package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.model.AddCarModelRequestDTO;
import com.sangto.rental_car_server.domain.entity.CarModel;
import com.sangto.rental_car_server.domain.mapper.CarModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarModelMapperImpl implements CarModelMapper {
    @Override
    public CarModel addCarModelRequestDTOtoEntity(AddCarModelRequestDTO requestDTO) {
        return CarModel.builder()
                .name(requestDTO.modelName())
                .carType(requestDTO.carType())
                .build();
    }
}
