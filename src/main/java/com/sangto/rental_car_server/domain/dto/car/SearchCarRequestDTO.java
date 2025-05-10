package com.sangto.rental_car_server.domain.dto.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sangto.rental_car_server.annotation.AfterNowTime;
import com.sangto.rental_car_server.annotation.RentalTimeMatching;
import com.sangto.rental_car_server.constant.TimeFormatConstant;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestParam;

@RentalTimeMatching(startTime = "startTime", endTime = "endTime")
public record SearchCarRequestDTO(
        @RequestParam(name = "address")
        @NotBlank(message = "Địa chỉ không được để trống")
        String address,
        @RequestParam(name = "startTime")
        @NotBlank(message = "The start rental time is not blank")
        @AfterNowTime(message = "The start rental time is after now")
        @JsonFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
        String startTime,
        @RequestParam(name = "endTime")
        @NotBlank(message = "The end rental time is not blank")
        @JsonFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
        String endTime
) {
}
