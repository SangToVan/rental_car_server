package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.mapper.CarMapper;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CarMapperImpl implements CarMapper {

    private final UserMapper userMapper;
    private final UserRepository userRepo;

    @Override
    public CarResponseDTO toCarResponseDTO(Car entity) {

        return CarResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .licensePlate(entity.getLicensePlate())
                .brand(entity.getBrand())
                .color(entity.getColor())
                .model(entity.getModel())
                .numberOfSeats(entity.getNumberOfSeats())
                .productionYear(entity.getProductionYear())
                .carStatus(entity.getCarStatus())
                .build();
    }

    @Override
    public CarDetailResponseDTO toCarDetailResponseDTO(Car entity) {

        return CarDetailResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .licensePlate(entity.getLicensePlate())
                .brand(entity.getBrand())
                .color(entity.getColor())
                .model(entity.getModel())
                .numberOfSeats(entity.getNumberOfSeats())
                .productionYear(entity.getProductionYear())
                .basePrice(entity.getBasePrice().toString())
                .carStatus(entity.getCarStatus())
                .build();
    }

    @Override
    public CarDetailResponseForOwnerDTO toCarDetailResponseForOwnerDTO(Car entity) {

        return CarDetailResponseForOwnerDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .licensePlate(entity.getLicensePlate())
                .brand(entity.getBrand())
                .color(entity.getColor())
                .model(entity.getModel())
                .numberOfSeats(entity.getNumberOfSeats())
                .productionYear(entity.getProductionYear())
                .basePrice(entity.getBasePrice().toString())
                .carStatus(entity.getCarStatus())
                .owner(userMapper.toUserDetailResponseDTO(entity.getCarOwner()))
                .build();
    }

    @Override
    public Car addCarRequestDTOtoEntity(AddCarRequestDTO requestDTO) {

        return Car.builder()
                .name(requestDTO.name())
                .licensePlate(requestDTO.licensePlate())
                .brand(requestDTO.brand())
                .color(requestDTO.color())
                .model(requestDTO.model())
                .numberOfSeats(requestDTO.numberOfSeats())
                .productionYear(requestDTO.productionYear())
                .basePrice(new BigDecimal(requestDTO.basePrice()))
                .carStatus(ECarStatus.UNVERIFIED)
                .build();
    }

    @Override
    public Car updCarRequestDTOtoEntity(Car oldCar, UpdCarRequestDTO requestDTO) {

        oldCar.setName(requestDTO.name());
        oldCar.setLicensePlate(requestDTO.licensePlate());
        oldCar.setBrand(requestDTO.brand());
        oldCar.setColor(requestDTO.color());
        oldCar.setModel(requestDTO.model());
        oldCar.setNumberOfSeats(requestDTO.numberOfSeats());
        oldCar.setProductionYear(requestDTO.productionYear());
        oldCar.setBasePrice(new BigDecimal(requestDTO.basePrice()));

        return oldCar;
    }
}
