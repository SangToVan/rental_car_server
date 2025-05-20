package com.sangto.rental_car_server.domain.dto.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sangto.rental_car_server.annotation.AfterNowTime;
import com.sangto.rental_car_server.annotation.RentalTimeMatching;
import com.sangto.rental_car_server.constant.TimeFormatConstant;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@RentalTimeMatching(startTime = "startTime", endTime = "endTime")
public record SearchCarRequestDTO(

        @RequestParam(name = "address")
        @NotBlank(message = "Địa chỉ không được để trống")
        String address,

        @RequestParam(name = "startTime")
        @NotBlank(message = "Thời gian bắt đầu không được để trống")
        @AfterNowTime(message = "Thời gian bắt đầu phải sau thời điểm hiện tại")
        @JsonFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
        String startTime,

        @RequestParam(name = "endTime")
        @NotBlank(message = "Thời gian kết thúc không được để trống")
        @JsonFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
        String endTime,

        @RequestParam(name = "brand", required = false)
        String brand,

        @RequestParam(name = "numberOfSeats", required = false)
        Integer numberOfSeats,

        @RequestParam(name = "transmission", required = false)
        String transmission,

        @RequestParam(name = "fuelType", required = false)
        String fuelType,

        @RequestParam(name = "minPrice", required = false)
        BigDecimal minPrice,

        @RequestParam(name = "maxPrice", required = false)
        BigDecimal maxPrice

) {}

