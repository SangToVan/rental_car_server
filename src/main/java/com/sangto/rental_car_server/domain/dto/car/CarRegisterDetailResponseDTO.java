package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.enums.ECarTransmission;
import com.sangto.rental_car_server.domain.enums.EFuelType;
import lombok.Builder;

import java.math.BigInteger;
import java.util.List;

@Builder
public record CarRegisterDetailResponseDTO(
        String name,
        String licensePlate,
        String brand,
        String model,
//    Integer modelId,
        String color,
        Integer numberOfSeats,
        Integer productionYear,
        ECarTransmission transmission,
        EFuelType fuelType,
        Integer mileage,
        Float fuelConsumption,
        String address,
        String description,
        String additionalFunctions,
        String termOfUse,
        BigInteger basePrice,
        Boolean quickRent,
        Integer maxDeliveryDistance,
        Integer deliveryFee,
        Integer freeDeliveryDistance,
        Integer kmPerDay,
        Integer kmOverDayFee,
        Integer discountPerWeek,
        List<ImageResponseDTO> images
) {
}
