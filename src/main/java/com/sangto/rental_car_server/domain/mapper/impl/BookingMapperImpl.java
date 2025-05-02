package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.booking.AddBookingRequestDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseForOwnerDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.mapper.BookingMapper;
import com.sangto.rental_car_server.domain.mapper.CarMapper;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.utility.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingMapperImpl implements BookingMapper {

    private final UserMapper userMapper;
    private final CarMapper carMapper;

    @Override
    public BookingResponseDTO toBookingResponseDTO(Booking entity) {

        return BookingResponseDTO.builder()
                .bookingId(entity.getId())
                .carId(entity.getCar().getId())
                .carName(entity.getCar().getName())
                .basePrice(entity.getCar().getBasePrice())
                .startDateTime(TimeUtil.formatToString(entity.getStartDateTime()))
                .endDateTime(TimeUtil.formatToString(entity.getEndDateTime()))
                .numberOfHour(entity.getNumberOfHours())
                .totalPrice(entity.getTotalPrice().toString())
                .rentalFee(entity.getRentalFee().toString())
                .bookingStatus(entity.getStatus())
                .build();
    }

    @Override
    public BookingDetailResponseDTO toBookingDetailResponseDTO(Booking entity) {

        return BookingDetailResponseDTO.builder()
                .bookingId(entity.getId())
                .carDetail(carMapper.toCarDetailResponseDTO(entity.getCar()))
                .customerInfo(userMapper.toUserDetailResponseDTO(entity.getUser()))
                .startDateTime(TimeUtil.formatToString(entity.getStartDateTime()))
                .endDateTime(TimeUtil.formatToString(entity.getEndDateTime()))
                .numberOfHour(entity.getNumberOfHours())
                .totalPrice(entity.getTotalPrice().toString())
                .rentalFee(entity.getRentalFee().toString())
                .paymentMethod(entity.getPaymentMethod())
                .status(entity.getStatus())
                .driverName(entity.getDriverName())
                .driverPhone(entity.getDriverPhone())
                .driverCitizenId(entity.getDriverCitizenId())
                .relationship(entity.getRelationship())
                .build();
    }

    @Override
    public BookingResponseForOwnerDTO toBookingResponseForOwnerDTO(Booking entity) {

        return BookingResponseForOwnerDTO.builder()
                .bookingId(entity.getId())
                .customerInfo(userMapper.toUserDetailResponseDTO(entity.getUser()))
                .startDateTime(TimeUtil.formatToString(entity.getStartDateTime()))
                .endDateTime(TimeUtil.formatToString(entity.getEndDateTime()))
                .paymentMethod(entity.getPaymentMethod())
                .driverName(entity.getDriverName())
                .driverPhone(entity.getDriverPhone())
                .driverCitizenId(entity.getDriverCitizenId())
                .relationship(entity.getRelationship())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public Booking addBookingRequestDTOtoEntity(AddBookingRequestDTO requestDTO) {

        return Booking.builder()
                .startDateTime(TimeUtil.convertToDateTime(requestDTO.startDateTime()))
                .endDateTime(TimeUtil.convertToDateTime(requestDTO.endDateTime()))
                .paymentMethod(requestDTO.paymentMethod())
                .driverName(requestDTO.driverName())
                .driverPhone(requestDTO.driverPhone())
                .driverCitizenId(requestDTO.driverCitizenId())
                .relationship(requestDTO.relationship())
                .status(EBookingStatus.PENDING)
                .build();
    }
}
