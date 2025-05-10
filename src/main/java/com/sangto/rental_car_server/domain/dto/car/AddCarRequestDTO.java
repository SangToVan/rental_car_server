package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.enums.ECarTransmission;
import com.sangto.rental_car_server.domain.enums.EFuelType;
import lombok.Builder;

@Builder
public record AddCarRequestDTO(
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
        String basePrice,
        Boolean quickRent,
        Integer maxDeliveryDistance,
        Integer deliveryFee,
        Integer freeDeliveryDistance,
        Integer kmPerDay,
        Integer kmOverDayFee,
        Integer discountPerWeek,
        String[] images
) {
}
