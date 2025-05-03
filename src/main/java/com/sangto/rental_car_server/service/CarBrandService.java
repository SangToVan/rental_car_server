package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.brand.AddCarBrandRequestDTO;
import com.sangto.rental_car_server.responses.Response;

public interface CarBrandService {
    Response<String> addCarBrand(AddCarBrandRequestDTO requestDTO);
}
