package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.responses.Response;

import java.util.List;

public interface CarService {
    Car verifyCarOwner(Integer ownerId, Integer carId);

    Response<List<CarResponseDTO>> getListCarsByOwnerId(Integer ownerId);

    Response<CarDetailResponseDTO> getCarDetail(Integer carId);

    Response<CarDetailResponseForOwnerDTO> getCarDetailForOwner(Integer carId);

    Response<CarDetailResponseDTO> addCar(Integer ownerId, AddCarRequestDTO requestDTO);

    Response<CarDetailResponseDTO> updateCar(Integer carId, UpdCarRequestDTO requestDTO);

    Response<String> verifyCar(Integer carId);

    Response<String> changeCarStatus(Integer carId);
}
