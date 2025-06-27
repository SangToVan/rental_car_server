package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.booking.AddBookingRequestDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingStatisticsResponseDTO;
import com.sangto.rental_car_server.domain.dto.car.AddCarRequestDTO;
import com.sangto.rental_car_server.domain.dto.feedback.AddFeedbackRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.payment.AddPaymentRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.dto.report.AddReportRequestDTO;
import com.sangto.rental_car_server.domain.entity.Feedback;
import com.sangto.rental_car_server.responses.MetaResponse;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.BookingService;
import com.sangto.rental_car_server.service.FeedbackService;
import com.sangto.rental_car_server.service.ReportService;
import com.sangto.rental_car_server.utility.AuthUtil;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Tag(name = "Bookings")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BookingController {
    private final BookingService bookingService;
    private final ReportService reportService;
    private final FeedbackService feedbackService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping(Endpoint.V1.Booking.LIST_FOR_USER)
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<BookingResponseDTO>>> getListBookingForUser(
            HttpServletRequest servletRequest, @ParameterObject MetaRequestDTO metaRequestDTO) {
        Integer userId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookingForUser(metaRequestDTO,userId));
    }

    @GetMapping(Endpoint.V1.Booking.LIST_UNFINISHED)
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<BookingResponseDTO>>> getListUnFinishedBookingForUser(
            HttpServletRequest servletRequest, @ParameterObject MetaRequestDTO metaRequestDTO) {
        Integer userId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getUnfinishedBookingForUser(metaRequestDTO,userId));
    }

    @GetMapping(Endpoint.V1.Booking.LIST_FINISHED)
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<BookingResponseDTO>>> getListFinishedBookingForUser(
            HttpServletRequest servletRequest, @ParameterObject MetaRequestDTO metaRequestDTO) {
        Integer userId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getFinishedBookingForUser(metaRequestDTO,userId));
    }

    @GetMapping(Endpoint.V1.Booking.STATISTICS)
    public ResponseEntity<Response<BookingStatisticsResponseDTO>> getBookingStatistics(
            HttpServletRequest servletRequest,
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr
    ) throws BadRequestException {
        Integer userId = Integer.valueOf(
                jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION))
        );

        // Parse string → LocalDateTime, cho phép null
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        try {
            if (startDateStr != null) {
                startDate = LocalDateTime.parse(startDateStr, formatter);
            }
            if (endDateStr != null) {
                endDate = LocalDateTime.parse(endDateStr, formatter);
            }
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Ngày không đúng định dạng yyyy-MM-dd'T'HH:mm:ss");
        }
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingStatistics(userId, startDate, endDate));
    }


    @GetMapping(Endpoint.V1.Booking.DETAILS)
    public ResponseEntity<Response<BookingDetailResponseDTO>> getDetailBooking(
            @PathVariable(name = "bookingId") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingDetail(bookingId));
    }

    @PostMapping(Endpoint.V1.Booking.BASE)
    public ResponseEntity<Response<BookingDetailResponseDTO>> addBooking(
            HttpServletRequest servletRequest, @RequestBody @Valid AddBookingRequestDTO requestDTO) throws MessagingException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.addBooking(AuthUtil.getRequestedUser().getId(), requestDTO));
    }

    @PatchMapping(Endpoint.V1.Booking.PAYMENT_BOOKING)
    public ResponseEntity<Response<PaymentResponseDTO>> paymentBooking(
            @PathVariable(name = "bookingId") Integer bookingId,
            @RequestBody @Valid AddPaymentRequestDTO requestDTO,
            HttpServletRequest request
    ) throws MessagingException {
        return ResponseEntity.ok(
                bookingService.paymentBooking(bookingId, AuthUtil.getRequestedUser().getId(), requestDTO, request)
        );
    }


    @PatchMapping(Endpoint.V1.Booking.CONFIRM_BOOKING)
    public ResponseEntity<Response<String>> confirmBooking(@PathVariable(name = "bookingId") Integer bookingId)
            throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.confirmBooking(bookingId, AuthUtil.getRequestedUser().getId()));
    }

    @PatchMapping(Endpoint.V1.Booking.CONFIRM_PICK_UP)
    public ResponseEntity<Response<String>> confirmPickUp(@PathVariable(name = "bookingId") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.confirmPickup(bookingId, AuthUtil.getRequestedUser().getId()));
    }

    @PatchMapping(Endpoint.V1.Booking.CONFIRM_RETURN)
    public ResponseEntity<Response<String>> confirmReturn(@PathVariable(name = "bookingId") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.confirmReturn(bookingId, AuthUtil.getRequestedUser().getId()));
    }

    @PatchMapping(Endpoint.V1.Booking.COMPLETE_BOOKING)
    public ResponseEntity<Response<String>> completeBooking(@PathVariable(name = "bookingId") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.completeBooking(bookingId));
    }

    @PatchMapping(Endpoint.V1.Booking.CANCELLED_BOOKING)
    public ResponseEntity<Response<String>> cancelBooking(@PathVariable(name = "bookingId") Integer bookingId)
            throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.cancelBooking(bookingId, AuthUtil.getRequestedUser().getId()));
    }

    @PostMapping(Endpoint.V1.Booking.REPORT)
    public ResponseEntity<Response<String>> reportBooking(@PathVariable(name = "bookingId") Integer bookingId, HttpServletRequest servletRequest, @RequestBody @Valid AddReportRequestDTO requestDTO) {
        Integer userId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));

        return ResponseEntity.status(HttpStatus.OK).body(reportService.addReport(userId, bookingId, requestDTO));
    }

    @PostMapping(Endpoint.V1.Booking.FEEDBACK)
    public ResponseEntity<Response<String>> giveRating(
            @PathVariable(name = "bookingId") Integer bookingId, @RequestBody @Valid AddFeedbackRequestDTO requestDTO) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(feedbackService.addFeedback(AuthUtil.getRequestedUser().getId(), bookingId, requestDTO));
    }
}
