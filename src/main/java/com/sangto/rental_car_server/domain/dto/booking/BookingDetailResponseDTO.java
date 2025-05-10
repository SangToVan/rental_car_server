package com.sangto.rental_car_server.domain.dto.booking;

import com.sangto.rental_car_server.domain.dto.car.CarDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.enums.*;
import lombok.Builder;

@Builder
public record BookingDetailResponseDTO(
        Integer bookingId,
        CarDetailResponseDTO carDetail,
        UserDetailResponseDTO customerInfo,
        String startDateTime,
        String endDateTime,
        Long numberOfDays,
        String totalPrice,
        String rentalFee,
        EPaymentMethod paymentMethod,
        EPaymentType paymentType, // MỚI
        EBookingStatus status,
        EDocumentRental documentRental,
        String driverName,
        String driverPhone,
        String driverCitizenId,
        ERelationship relationship,

        // Các trường tài chính MỚI
        String depositAmount,
        String totalPaidAmount,
        String needToPayInCash,
        String refundAmount,
        String payoutAmount,
        boolean isRefunded,
        boolean isPayoutDone
) {
}

