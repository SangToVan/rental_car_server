package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.escrow_transaction.AddEscrowTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.EPaymentStatus;
import com.sangto.rental_car_server.domain.enums.EscrowStatus;
import com.sangto.rental_car_server.domain.mapper.PaymentMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.PaymentRepository;
import com.sangto.rental_car_server.service.EscrowTransactionService;
import com.sangto.rental_car_server.service.VNPayService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "Payments")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class PaymentController {

    private final PaymentRepository paymentRepo;
    private final BookingRepository bookingRepo;
    private final VNPayService vnPayService;
    private final EscrowTransactionService escrowTransactionService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @GetMapping(Endpoint.V1.Payment.VNPAY_RETURN)
    public ResponseEntity<Void> vnpayReturn(HttpServletRequest request) {
        Map<String, String> vnpParams = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> vnpParams.put(k, v[0]));

        String txnRef = vnpParams.get("vnp_TxnRef");
        String vnpResponseCode = vnpParams.get("vnp_ResponseCode");
        String vnpTransactionStatus = vnpParams.getOrDefault("vnp_TransactionStatus", "00");

        Payment payment = paymentRepo.findByTransactionCodeWithBooking(txnRef)
                .orElseThrow(() -> new AppException("Không tìm thấy thanh toán"));

        if (payment.getPaymentStatus() == EPaymentStatus.PENDING
                && "00".equals(vnpResponseCode) && "00".equals(vnpTransactionStatus)) {

            payment.setPaymentStatus(EPaymentStatus.SUCCESS);
            payment.setUpdatedAt(LocalDateTime.now());

            Booking booking = payment.getBooking();
            BigDecimal total = booking.getTotalPaidAmount() == null
                    ? payment.getAmount()
                    : booking.getTotalPaidAmount().add(payment.getAmount());
            booking.setTotalPaidAmount(total);

            if ((total.compareTo(booking.getTotalPrice()) >= 0 || total.compareTo(booking.getDepositAmount()) >= 0)
                    && booking.getStatus() == EBookingStatus.PENDING) {
                booking.setStatus(EBookingStatus.PAID);
            }

            bookingRepo.save(booking);
            paymentRepo.save(payment);

            escrowTransactionService.addEscrowTransaction(AddEscrowTransactionRequestDTO.builder()
                    .status(EscrowStatus.PENDING)
                    .amount(payment.getAmount())
                    .bookingId(booking.getId())
                    .build());
        } else if (payment.getPaymentStatus() == EPaymentStatus.PENDING) {
            payment.setPaymentStatus("EXPIRED".equals(vnpResponseCode)
                    ? EPaymentStatus.EXPIRED
                    : EPaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepo.save(payment);
        }

        // Tạo redirect URL về FE
        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendUrl + "/payment-status")
                .queryParam("status", payment.getPaymentStatus())
                .queryParam("bookingId", payment.getBooking().getId())
                .queryParam("amount", payment.getAmount().toPlainString())
                .queryParam("paymentMethod", payment.getPaymentMethod())
                .queryParam("paymentType", payment.getPaymentType())
                .queryParam("transactionCode", payment.getTransactionCode())
                .build()
                .toUriString();

        return ResponseEntity.status(302).location(URI.create(redirectUrl)).build();
    }

}
