package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.model.AddCarModelRequestDTO;
import com.sangto.rental_car_server.responses.Response;

public interface CarModelService {
    Response<String> addCarModel(AddCarModelRequestDTO requestDTO);
}
