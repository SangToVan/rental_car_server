package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.enums.ECarTransmission;
import com.sangto.rental_car_server.domain.enums.EFuelType;
import lombok.Builder;

import java.math.BigInteger;
import java.util.List;

@Builder
public record CarResponseDTO(
        Integer carId,
        String name,
        String brand,
        String model,
        ECarTransmission transmission,
        Integer numberOfSeats,
        EFuelType fuelType,
        String address,
        BigInteger basePrice,
        Double rating,
        Integer bookingCount,
        List<ImageResponseDTO> images
) {
}
