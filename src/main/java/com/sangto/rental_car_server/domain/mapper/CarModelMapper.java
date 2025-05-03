package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.model.AddCarModelRequestDTO;
import com.sangto.rental_car_server.domain.entity.CarModel;

public interface CarModelMapper {
    CarModel addCarModelRequestDTOtoEntity(AddCarModelRequestDTO requestDTO);
}
