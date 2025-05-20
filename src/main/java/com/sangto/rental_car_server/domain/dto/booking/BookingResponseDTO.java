package com.sangto.rental_car_server.domain.dto.booking;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record BookingResponseDTO(
        Integer bookingId,
        EBookingStatus status,
        List<ImageResponseDTO> images,
        String carName,
        String startDateTime,
        String endDateTime,
        String bookingDate,
        String totalPrice,
        String totalPaidAmount,
        String needToPayInCash,
        String ownerAvatar,
        String ownerName
) {
}
