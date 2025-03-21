package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.booking.AddBookingRequestDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseForOwnerDTO;
import com.sangto.rental_car_server.domain.entity.Booking;

public interface BookingMapper {

    BookingResponseDTO toBookingResponseDTO(Booking entity);

    BookingDetailResponseDTO toBookingDetailResponseDTO(Booking entity);

    BookingResponseForOwnerDTO toBookingResponseForOwnerDTO(Booking entity);

    Booking addBookingRequestDTOtoEntity(AddBookingRequestDTO requestDTO);

}
