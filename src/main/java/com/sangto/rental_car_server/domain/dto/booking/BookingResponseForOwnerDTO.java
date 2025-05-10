package com.sangto.rental_car_server.domain.dto.booking;

import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.EDocumentRental;
import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.ERelationship;
import lombok.Builder;

@Builder
public record BookingResponseForOwnerDTO(
        Integer bookingId,
        UserDetailResponseDTO customerInfo,
        String startDateTime,
        String endDateTime,
        EPaymentMethod paymentMethod,
        EDocumentRental documentRental,
        String driverName,
        String driverPhone,
        String driverCitizenId,
        ERelationship relationship,
        EBookingStatus status
) {
}
