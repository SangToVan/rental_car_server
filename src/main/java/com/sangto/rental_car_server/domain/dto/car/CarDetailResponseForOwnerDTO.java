package com.sangto.rental_car_server.domain.dto.car;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.enums.ECarTransmission;
import com.sangto.rental_car_server.domain.enums.EFuelType;
import lombok.Builder;

import java.util.List;

@Builder
public record CarDetailResponseForOwnerDTO(
        Integer carId,
        //Basic information(can't change)
        String name,
        String licensePlate,
        String brand,
        String model,
        String color,
        Integer numberOfSeats,
        Integer productionYear,
        ECarTransmission transmission,
        EFuelType fuelType,
        String createdAt,
        String updatedAt,
        Double rating,
        Integer completeBookingCount,
        Integer inProgressBookingCount,
        Integer pendingBookingCount,
        ECarStatus carStatus,
        List<ImageResponseDTO> images,
        // Information (Can change)
        Integer mileage,
        Float fuelConsumption,
        String address,
        String description,
        String additionalFunctions,
        String termOfUse,

        // Price information (Can change)
        String basePrice,
        Boolean quickRent,
        Integer maxDeliveryDistance,
        Integer deliveryFee,
        Integer freeDeliveryDistance,
        Integer kmPerDay,
        Integer kmOverDayFee,
        Integer discountPerWeek

) {
}
