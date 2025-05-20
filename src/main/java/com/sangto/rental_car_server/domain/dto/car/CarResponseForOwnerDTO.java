package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import lombok.Builder;

import java.math.BigInteger;
import java.util.List;

@Builder
public record CarResponseForOwnerDTO(
        Integer carId,
        ECarStatus status,
        String name,
        String address,
        Double rating,
        Integer completeBookingCount,
        Integer inProgressBookingCount,
        Integer pendingBookingCount,
        BigInteger basePrice,
        List<ImageResponseDTO> images
) {
}
