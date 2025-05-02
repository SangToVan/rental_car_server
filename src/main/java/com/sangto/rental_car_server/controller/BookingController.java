package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.booking.AddBookingRequestDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.BookingService;
import com.sangto.rental_car_server.utility.AuthUtil;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Bookings")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BookingController {
    private final BookingService bookingService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping(Endpoint.V1.Booking.LIST_FOR_USER)
    public ResponseEntity<Response<List<BookingResponseDTO>>> getListBookingForUser(
            HttpServletRequest servletRequest) {
        Integer userId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookingForUser(userId));
    }

    @GetMapping(Endpoint.V1.Booking.DETAILS)
    public ResponseEntity<Response<BookingDetailResponseDTO>> getDetailBooking(
            @PathVariable(name = "id") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingDetail(bookingId));
    }

    @PostMapping(Endpoint.V1.Booking.BASE)
    public ResponseEntity<Response<BookingDetailResponseDTO>> addBooking(
            HttpServletRequest servletRequest, @RequestBody @Valid AddBookingRequestDTO requestDTO) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.addBooking(AuthUtil.getRequestedUser().getId(), requestDTO));
    }

    @PatchMapping(Endpoint.V1.Booking.PAYMENT_BOOKING)
    public ResponseEntity<Response<String>> paymentBooking(@PathVariable(name = "id") Integer bookingId)
            throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.paymentBooking(bookingId, AuthUtil.getRequestedUser().getId()));
    }

    @PatchMapping(Endpoint.V1.Booking.CONFIRM_BOOKING)
    public ResponseEntity<Response<String>> confirmBooking(@PathVariable(name = "id") Integer bookingId)
            throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.confirmBooking(bookingId, AuthUtil.getRequestedUser().getId()));
    }

    @PatchMapping(Endpoint.V1.Booking.CONFIRM_PICK_UP)
    public ResponseEntity<Response<String>> confirmPickUp(@PathVariable(name = "id") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.confirmPickup(bookingId, AuthUtil.getRequestedUser().getId()));
    }

    @PatchMapping(Endpoint.V1.Booking.CONFIRM_RETURN)
    public ResponseEntity<Response<String>> confirmReturn(@PathVariable(name = "id") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.confirmReturn(bookingId, AuthUtil.getRequestedUser().getId()));
    }

    @PatchMapping(Endpoint.V1.Booking.COMPLETE_BOOKING)
    public ResponseEntity<Response<String>> completeBooking(@PathVariable(name = "id") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.completeBooking(bookingId));
    }

    @PatchMapping(Endpoint.V1.Booking.CANCELLED_BOOKING)
    public ResponseEntity<Response<String>> cancelBooking(@PathVariable(name = "id") Integer bookingId)
            throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.cancelBooking(AuthUtil.getRequestedUser().getId(), bookingId));
    }
}
