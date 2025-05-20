package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.entity.Car;

import java.time.LocalDateTime;

public interface CarMapper {
    CarResponseDTO toCarResponseDTO(Car entity);

    CarResponseForOwnerDTO toCarResponseForOwnerDTO(Car entity);

    CarDetailResponseDTO toCarDetailResponseDTO(Car entity, Integer userId);

    CarDetailResponseForBookingDTO toCarDetailResponseForBookingDTO(Car entity, Integer userId, String startDateTime, String endDateTime);

    CarRegisterDetailResponseDTO toCarRegisterDetailResponseDTO(Car entity);

    CarDetailResponseForOwnerDTO toCarDetailResponseForOwnerDTO(Car entity);

    Car addCarRequestDTOtoEntity(AddCarRequestDTO requestDTO);

    Car registerRequestDTOtoEntity(Car oldCar, AddCarRequestDTO requestDTO);

    Car updCarRequestDTOtoEntity(Car oldCar, UpdCarRequestDTO requestDTO);

    Car updCarInfoRequestDTOtoEntity(Car oldCar, UpdCarInfoRequestDTO requestDTO);

    Car updCarPricingRequestDTOtoEntity(Car oldCar, UpdCarPricingRequestDTO requestDTO);
}
