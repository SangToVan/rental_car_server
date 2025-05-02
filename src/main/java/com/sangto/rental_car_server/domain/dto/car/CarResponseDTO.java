package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record CarResponseDTO(
        Integer id,
        String name,
        String licensePlate,
        String brand,
        String model,
        String address,
        String basePrice,
        ECarStatus carStatus,
        Double rating,
        List<ImageResponseDTO> images
) {
}
