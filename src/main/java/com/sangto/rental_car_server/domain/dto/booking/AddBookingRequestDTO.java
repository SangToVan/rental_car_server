package com.sangto.rental_car_server.domain.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sangto.rental_car_server.annotation.AfterNowTime;
import com.sangto.rental_car_server.annotation.RentalTimeMatching;
import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.enums.EDocumentRental;
import com.sangto.rental_car_server.domain.enums.ERelationship;
import jakarta.validation.constraints.NotBlank;

@RentalTimeMatching(startTime = "startDateTime", endTime = "endDateTime")
public record AddBookingRequestDTO(
        Integer carId,
        @NotBlank(message = "The start rental time is not blank")
        @AfterNowTime(message = "The start rental time is after now")
        @JsonFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
        String startDateTime,
        @NotBlank(message = "The end rental time is not blank")
        @JsonFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
        String endDateTime,
        EDocumentRental documentRental,
        String driverName,
        String driverPhone,
        String driverCitizenId,
        ERelationship relationship
) {
}
