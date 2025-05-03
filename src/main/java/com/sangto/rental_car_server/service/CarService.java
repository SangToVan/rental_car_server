package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.responses.MetaResponse;
import com.sangto.rental_car_server.responses.Response;

import java.util.List;

public interface CarService {
    Car verifyCarOwner(Integer ownerId, Integer carId);

    MetaResponse<MetaResponseDTO, List<CarResponseDTO>> getListCarsByOwnerId(MetaRequestDTO metaRequestDTO, Integer ownerId);

    MetaResponse<MetaResponseDTO, List<CarResponseDTO>> getAllCars(MetaRequestDTO metaRequestDTO);

    Response<CarDetailResponseDTO> getCarDetail(Integer carId);

    Response<CarDetailResponseForOwnerDTO> getCarDetailForOwner(Integer carId);

    Response<CarDetailResponseDTO> addCar(Integer ownerId, AddCarRequestDTO requestDTO);

    Response<CarDetailResponseDTO> updateCar(Integer carId, UpdCarRequestDTO requestDTO);

    Response<String> verifyCar(Integer carId);

    Response<String> changeCarStatus(Integer carId);

    MetaResponse<MetaResponseDTO, List<CarResponseDTO>> searchCarV2(
            String address, String startTime, String endTime, MetaRequestDTO metaRequestDTO);
}
