package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.booking.AddBookingRequestDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseForOwnerDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.responses.Response;
import jakarta.mail.MessagingException;

import java.util.List;

public interface BookingService {
    Booking verifyBookingCarOwner(Integer ownerId, Integer bookingId);

    Booking verifyBookingCustomer(Integer customerId, Integer bookingId);

    Response<List<BookingResponseDTO>> getAllBookings();

    Response<List<BookingResponseDTO>> getAllBookingForUser(Integer userId);

    Response<List<BookingResponseForOwnerDTO>> getAllBookingForCar(Integer carId, Integer ownerId);

    Response<BookingDetailResponseDTO> getBookingDetail(Integer bookingId);

    Response<BookingDetailResponseDTO> addBooking(Integer userId, AddBookingRequestDTO requestDTO) throws MessagingException;

    Response<String> paymentBooking(Integer bookingId, Integer userId);

    Response<String> confirmBooking(Integer bookingId, Integer userId) throws MessagingException;

    Response<String> confirmPickup(Integer bookingId, Integer userId);

    Response<String> confirmReturn(Integer bookingId, Integer userId);

    Response<String> completeBooking(Integer bookingId);

    Response<String> cancelBooking(Integer bookingId, Integer userId) throws MessagingException;

}
