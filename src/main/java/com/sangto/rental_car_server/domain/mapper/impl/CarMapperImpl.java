package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.mapper.CarMapper;
import com.sangto.rental_car_server.domain.mapper.ImageMapper;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CarMapperImpl implements CarMapper {

    private final UserMapper userMapper;
    private final ImageMapper imageMapper;
    //private final CarModelRepository carModelRepo;
    private final BookingRepository bookingRepo;

    @Override
    public CarResponseDTO toCarResponseDTO(Car entity) {
        List<ImageResponseDTO> imageList = entity.getImages().stream()
                .map(imageMapper::toImageResponseDTO)
                .toList();
        return CarResponseDTO.builder()
                .carId(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .transmission(entity.getTransmission())
                .numberOfSeats(entity.getNumberOfSeats())
                .fuelType(entity.getFuelType())
                .address(entity.getAddress())
                .basePrice(entity.getBasePrice().toBigInteger())
                .rating(entity.getRating())
                .bookingCount(bookingRepo.countCompletedBookingsByCarId(entity.getId()))
                .images(imageList)
                .build();
    }

    @Override
    public CarDetailResponseDTO toCarDetailResponseDTO(Car entity) {
        List<ImageResponseDTO> imageList = entity.getImages().stream()
                .map(imageMapper::toImageResponseDTO)
                .toList();
        return CarDetailResponseDTO.builder()
                .carId(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .color(entity.getColor())
                .numberOfSeats(entity.getNumberOfSeats())
                .productionYear(entity.getProductionYear())
                .transmission(entity.getTransmission())
                .fuelType(entity.getFuelType())
                .mileage(entity.getMileage())
                .fuelConsumption(entity.getFuelConsumption())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .additionalFunctions(entity.getAdditionalFunctions())
                .termOfUse(entity.getTermsOfUse())
                .createdAt(entity.getCreatedAt().toString())
                .updatedAt(entity.getUpdatedAt().toString())
                .basePrice(entity.getBasePrice().toBigInteger())
                .status(entity.getStatus())
                .rating(entity.getRating())
                .bookingCount(bookingRepo.countCompletedBookingsByCarId(entity.getId()))
                .carOwner(userMapper.toUserDetailResponseDTO(entity.getCarOwner()))
                .images(imageList)
                .build();
    }

    @Override
    public CarDetailResponseForOwnerDTO toCarDetailResponseForOwnerDTO(Car entity) {
        List<ImageResponseDTO> imageList = entity.getImages().stream()
                .map(imageMapper::toImageResponseDTO)
                .toList();
        return CarDetailResponseForOwnerDTO.builder()
                .carId(entity.getId())
                .name(entity.getName())
                .licensePlate(entity.getLicensePlate())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .color(entity.getColor())
                .numberOfSeats(entity.getNumberOfSeats())
                .productionYear(entity.getProductionYear())
                .transmission(entity.getTransmission())
                .fuelType(entity.getFuelType())
                .mileage(entity.getMileage())
                .fuelConsumption(entity.getFuelConsumption())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .additionalFunctions(entity.getAdditionalFunctions())
                .termOfUse(entity.getTermsOfUse())
                .createdAt(entity.getCreatedAt().toString())
                .updatedAt(entity.getUpdatedAt().toString())
                .basePrice(entity.getBasePrice().toBigInteger().toString())
                .carStatus(entity.getStatus())
                .rating(entity.getRating())
                .carOwner(userMapper.toUserDetailResponseDTO(entity.getCarOwner()))
                .images(imageList)
                .build();
    }

    @Override
    public Car addCarRequestDTOtoEntity(AddCarRequestDTO requestDTO) {

        return Car.builder()
                .name(requestDTO.name())
                .licensePlate(requestDTO.licensePlate())
//                .model(carModelRepo.findById(requestDTO.modelId()).orElse(null))
                .brand(requestDTO.brand())
                .model(requestDTO.model())
                .color(requestDTO.color())
                .numberOfSeats(requestDTO.numberOfSeats())
                .productionYear(requestDTO.productionYear())
                .transmission(requestDTO.transmission())
                .fuelType(requestDTO.fuelType())
                .mileage(requestDTO.mileage())
                .fuelConsumption(requestDTO.fuelConsumption())
                .address(requestDTO.address())
                .description(requestDTO.description())
                .additionalFunctions(requestDTO.additionalFunctions())
                .termsOfUse(requestDTO.termOfUse())
                .basePrice(new BigDecimal(requestDTO.basePrice()).multiply(new BigDecimal(1000)))
                .quickRent(requestDTO.quickRent())
                .maxDeliveryDistance(requestDTO.maxDeliveryDistance())
                .deliveryFee(requestDTO.deliveryFee() * 1000)
                .freeDeliveryDistance(requestDTO.freeDeliveryDistance())
                .kmPerDay(requestDTO.kmPerDay())
                .kmOverDayFee(requestDTO.kmOverDayFee() * 1000)
                .discountPerWeek(requestDTO.discountPerWeek())
                .status(ECarStatus.ACTIVE)
                .rating(null)
                .images(new ArrayList<>())
                .build();
    }

    @Override
    public Car updCarRequestDTOtoEntity(Car oldCar, UpdCarRequestDTO requestDTO) {

        oldCar.setMileage(requestDTO.mileage());
        oldCar.setFuelConsumption(requestDTO.fuelConsumption());
        oldCar.setAddress(requestDTO.address());
        oldCar.setDescription(requestDTO.description());
        oldCar.setAdditionalFunctions(requestDTO.additionalFunctions());
        oldCar.setTermsOfUse(requestDTO.termOfUse());
        oldCar.setBasePrice(new BigDecimal(requestDTO.basePrice()));

        return oldCar;
    }
}
