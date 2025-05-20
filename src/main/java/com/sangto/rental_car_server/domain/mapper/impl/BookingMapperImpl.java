package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.booking.AddBookingRequestDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseForOwnerDTO;
import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.mapper.BookingMapper;
import com.sangto.rental_car_server.domain.mapper.CarMapper;
import com.sangto.rental_car_server.domain.mapper.ImageMapper;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.utility.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingMapperImpl implements BookingMapper {

    private final UserMapper userMapper;
    private final CarMapper carMapper;
    private final ImageMapper imageMapper;

    @Override
    public BookingResponseDTO toBookingResponseDTO(Booking entity) {

        List<ImageResponseDTO> imageList = entity.getCar().getImages().stream()
                .map(imageMapper::toImageResponseDTO)
                .toList();

        return BookingResponseDTO.builder()
                .bookingId(entity.getId())
                .status(entity.getStatus())
                .images(imageList)
                .carName(entity.getCar().getName())
                .startDateTime(entity.getStartDateTime().toString())
                .endDateTime(entity.getEndDateTime().toString())
                .bookingDate(entity.getBookingDate().toString())
                .totalPrice(entity.getTotalPrice().toString())
                .totalPaidAmount(entity.getTotalPaidAmount().toString())
                .needToPayInCash(entity.getNeedToPayInCash().toString())
                .ownerAvatar(entity.getCar().getCarOwner().getAvatar())
                .ownerName(entity.getCar().getCarOwner().getUsername())
                .build();
    }

    @Override
    public BookingDetailResponseDTO toBookingDetailResponseDTO(Booking entity) {

        return BookingDetailResponseDTO.builder()
                .bookingId(entity.getId())
                .carDetail(carMapper.toCarDetailResponseDTO(entity.getCar(), entity.getUser().getId()))
                .customerInfo(userMapper.toUserDetailResponseDTO(entity.getUser()))
                .bookingDate(entity.getBookingDate().toString())
                .startDateTime(TimeUtil.formatToString(entity.getStartDateTime()))
                .endDateTime(TimeUtil.formatToString(entity.getEndDateTime()))
                .numberOfDays(entity.getNumberOfDays())
                .totalPrice(entity.getTotalPrice().toString())
                .rentalFee(entity.getRentalFee().toString())
                .paymentMethod(entity.getPaymentMethod())
                .status(entity.getStatus())
                .documentRental(entity.getDocumentRental())
                .driverName(entity.getDriverName())
                .driverPhone(entity.getDriverPhone())
                .driverCitizenId(entity.getDriverCitizenId())
                .relationship(entity.getRelationship())
                .depositAmount(entity.getDepositAmount().toString())
                .totalPaidAmount(entity.getTotalPaidAmount().toString())
                .needToPayInCash(entity.getNeedToPayInCash().toString())
                .refundAmount(entity.getRefundAmount().toString())
                .payoutAmount(entity.getPayoutAmount().toString())
                .isRefunded(entity.isRefunded())
                .isPayoutDone(entity.isPayoutDone())
                .build();
    }

    @Override
    public BookingResponseForOwnerDTO toBookingResponseForOwnerDTO(Booking entity) {

        return BookingResponseForOwnerDTO.builder()
                .bookingId(entity.getId())
                .customerInfo(userMapper.toUserDetailResponseDTO(entity.getUser()))
                .status(entity.getStatus())
                .startDateTime(TimeUtil.formatToString(entity.getStartDateTime()))
                .endDateTime(TimeUtil.formatToString(entity.getEndDateTime()))
                .bookingDate(TimeUtil.formatToString(entity.getBookingDate()))
                .paymentMethod(entity.getPaymentMethod())
                .paymentMethod(entity.getPaymentMethod())
                .totalPrice(entity.getTotalPrice().toString())
                .totalPaidAmount(entity.getTotalPaidAmount().toString())
                .needToPayInCash(entity.getNeedToPayInCash().toString())
                .build();
    }

    @Override
    public Booking addBookingRequestDTOtoEntity(AddBookingRequestDTO requestDTO) {

        return Booking.builder()
                .startDateTime(TimeUtil.convertToDateTime(requestDTO.startDateTime()))
                .endDateTime(TimeUtil.convertToDateTime(requestDTO.endDateTime()))
                .documentRental(requestDTO.documentRental())
                .driverName(requestDTO.driverName())
                .driverPhone(requestDTO.driverPhone())
                .driverCitizenId(requestDTO.driverCitizenId())
                .relationship(requestDTO.relationship())
                .status(EBookingStatus.PENDING)
                .build();
    }
}
