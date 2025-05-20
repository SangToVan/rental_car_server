package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.booking.AddBookingRequestDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseForOwnerDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.payment.AddPaymentRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.responses.MetaResponse;
import com.sangto.rental_car_server.responses.Response;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BookingService {
    Booking verifyBookingCarOwner(Integer ownerId, Integer bookingId);

    Booking verifyBookingCustomer(Integer customerId, Integer bookingId);

    MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getAllBookings(MetaRequestDTO metaRequestDTO);

    MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getAllBookingForUser(MetaRequestDTO metaRequestDTO, Integer userId);

    MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getUnfinishedBookingForUser(MetaRequestDTO metaRequestDTO, Integer userId);

    MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getFinishedBookingForUser(MetaRequestDTO metaRequestDTO, Integer userId);

    MetaResponse<MetaResponseDTO, List<BookingResponseForOwnerDTO>> getAllBookingForCar(MetaRequestDTO metaRequestDTO, Integer carId, Integer ownerId);

    MetaResponse<MetaResponseDTO, List<BookingResponseForOwnerDTO>> getAllBookingForCarByStatus(MetaRequestDTO metaRequestDTO, Integer carId, Integer ownerId, EBookingStatus status);

    Response<BookingDetailResponseDTO> getBookingDetail(Integer bookingId);

    Response<BookingDetailResponseDTO> addBooking(Integer userId, AddBookingRequestDTO requestDTO) throws MessagingException;

    Response<PaymentResponseDTO> paymentBooking(Integer bookingId, Integer userId, AddPaymentRequestDTO requestDTO, HttpServletRequest request) throws MessagingException;

    Response<String> confirmBooking(Integer bookingId, Integer userId) throws MessagingException;

    Response<String> confirmPickup(Integer bookingId, Integer userId);

    Response<String> confirmReturn(Integer bookingId, Integer userId);

    Response<String> completeBooking(Integer bookingId);

    Response<String> cancelBooking(Integer bookingId, Integer userId) throws MessagingException;

    Response<String> customerCancelBooking(Integer bookingId, Integer userId) throws MessagingException;

    Response<String> ownerCancelBooking(Integer bookingId, Integer userId) throws MessagingException;

    Response<String> adminCancelBooking(Integer bookingId) throws MessagingException;

}
