package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.enums.ECarTransmission;
import com.sangto.rental_car_server.domain.enums.EFuelType;
import lombok.Builder;

import java.math.BigInteger;
import java.util.List;

@Builder
public record CarDetailResponseDTO(
        Integer carId,
        String name,
        String brand,
        String model,
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
        String createdAt,
        String updatedAt,
        BigInteger basePrice,
        ECarStatus status,
        Double rating,
        Integer bookingCount,
        List<ImageResponseDTO> images,
        UserDetailResponseDTO carOwner
) {
}
