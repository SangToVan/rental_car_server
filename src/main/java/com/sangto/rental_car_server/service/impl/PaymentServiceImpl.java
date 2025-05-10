package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.escrow_transaction.AddEscrowTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.AddPaymentRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.*;
import com.sangto.rental_car_server.domain.mapper.PaymentMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.PaymentRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.EscrowTransactionService;
import com.sangto.rental_car_server.service.PaymentService;
import com.sangto.rental_car_server.service.VNPayService;
import com.sangto.rental_car_server.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;
    private final PaymentRepository paymentRepo;
    private final PaymentMapper paymentMapper;
    private final WalletService walletService;
    private final VNPayService vnPayService;
    private final EscrowTransactionService escrowTransactionService;

    @Override
    public Response<List<PaymentResponseDTO>> getListByUserId(Integer userId) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("This user is not exist");
        User user = findUser.get();

        List<Payment> list = paymentRepo.findAllByUserId(user.getId());
        List<PaymentResponseDTO> li = list.stream().map(paymentMapper :: toPaymentResponseDTO).toList();

        return Response.successfulResponse(
                "Get list payment successfully",
                li
        );
    }

    @Override
    public PaymentResponseDTO addPayment(Integer bookingId, AddPaymentRequestDTO requestDTO, HttpServletRequest request) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new AppException("Booking is not exist"));

        User customer = booking.getUser();

        BigDecimal amount;
        if (requestDTO.paymentType() == EPaymentType.DEPOSIT) {
            amount = booking.getTotalPrice().multiply(new BigDecimal("0.4")).setScale(2, RoundingMode.HALF_UP);
            booking.setDepositAmount(amount);
            booking.setNeedToPayInCash(booking.getTotalPrice().subtract(amount));
        } else if (requestDTO.paymentType() == EPaymentType.FULL) {
            amount = booking.getTotalPrice();
            booking.setDepositAmount(amount);
            booking.setNeedToPayInCash(BigDecimal.ZERO);
        } else {
            throw new AppException("Invalid payment type");
        }

        String transactionCode = "TXN" + System.currentTimeMillis() + new Random().nextInt(1000);

        Payment payment = paymentMapper.addPaymentRequestDTOtoEntity(requestDTO);
        payment.setUser(customer);
        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setTransactionCode(transactionCode);
        payment.setPaymentStatus(EPaymentStatus.PENDING);

        Payment savedPayment = paymentRepo.save(payment);
        String payUrl = null;

        try {
            if (requestDTO.paymentMethod() == EPaymentMethod.WALLET) {
                // Xử lý ví
                walletService.paymentBooking(customer.getId(), amount, booking.getId());

                // Cập nhật trạng thái
                savedPayment.setPaymentStatus(EPaymentStatus.SUCCESS);
                savedPayment.setUpdatedAt(LocalDateTime.now());
                paymentRepo.save(savedPayment);

                // Cập nhật booking
                booking.setTotalPaidAmount(
                        booking.getTotalPaidAmount() == null ? amount : booking.getTotalPaidAmount().add(amount)
                );
                if ((booking.getTotalPaidAmount().compareTo(booking.getTotalPrice()) >= 0
                        || booking.getTotalPaidAmount().compareTo(booking.getDepositAmount()) >= 0)
                        && booking.getStatus() == EBookingStatus.PENDING) {
                    booking.setStatus(EBookingStatus.PAID);
                }
                bookingRepo.save(booking);

                // ✅ Chỉ tạo Escrow nếu thanh toán thực sự thành công
                AddEscrowTransactionRequestDTO escrowRequestDTO = AddEscrowTransactionRequestDTO.builder()
                        .status(EscrowStatus.PENDING)
                        .amount(amount)
                        .bookingId(booking.getId())
                        .build();
                escrowTransactionService.addEscrowTransaction(escrowRequestDTO);
            }

            else if (requestDTO.paymentMethod() == EPaymentMethod.BANK) {
                // Chỉ tạo paymentUrl, không tạo escrow ở đây
                payUrl = vnPayService.createPaymentUrl(savedPayment, request);
            }

        } catch (AppException e) {
            throw new AppException("Thanh toán thất bại: " + e.getMessage());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        PaymentResponseDTO responseDTO = paymentMapper.toPaymentResponseDTO(savedPayment);

        return PaymentResponseDTO.builder()
                .paymentId(responseDTO.paymentId())
                .bookingId(responseDTO.bookingId())
                .amount(responseDTO.amount())
                .paymentMethod(responseDTO.paymentMethod())
                .paymentType(responseDTO.paymentType())
                .paymentStatus(responseDTO.paymentStatus())
                .transactionCode(responseDTO.transactionCode())
                .paymentUrl(payUrl)
                .createdAt(responseDTO.createdAt())
                .build();
    }



    @Override
    public PaymentResponseDTO releasePayment(Integer bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new AppException("Booking is not exist"));

        BigDecimal payoutAmount = booking.getTotalPaidAmount(); // hoặc áp dụng trừ phí nếu cần

        try {
            walletService.releaseBooking(
                    booking.getCar().getCarOwner().getId(),
                    payoutAmount,
                    booking.getId()
            );
        } catch (AppException e) {
            throw new AppException(e.getMessage());
        }

        // Tạo bản ghi Payment mới cho RELEASE
        Payment payout = Payment.builder()
                .booking(booking)
                .user(booking.getUser()) // hoặc system user nếu bạn muốn
                .amount(payoutAmount)
                .paymentMethod(EPaymentMethod.WALLET) // hoặc BANK nếu cần
                .paymentStatus(EPaymentStatus.SUCCESS)
                .paymentType(EPaymentType.RELEASE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Payment savedPayment = paymentRepo.save(payout);

        // Cập nhật booking
        booking.setPayoutDone(true);
        booking.setPayoutAmount(payoutAmount);
        bookingRepo.save(booking);

        // Cập nhật escrow
        escrowTransactionService.updateEscrowStatus(booking.getId(), EscrowStatus.RELEASED);

        return paymentMapper.toPaymentResponseDTO(savedPayment);
    }


    @Override
    public PaymentResponseDTO refundPayment(Integer bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new AppException("Booking is not exist"));

        BigDecimal refundAmount = booking.getTotalPaidAmount(); // hoặc booking.getDepositAmount()

        try {
            walletService.refundBooking(
                    booking.getUser().getId(),
                    refundAmount,
                    booking.getId()
            );
        } catch (AppException e) {
            throw new AppException(e.getMessage());
        }

        // Tạo bản ghi Payment mới cho REFUND
        Payment refund = Payment.builder()
                .booking(booking)
                .user(booking.getUser())
                .amount(refundAmount)
                .paymentMethod(EPaymentMethod.WALLET) // hoặc BANK nếu hoàn tiền VNPAY
                .paymentStatus(EPaymentStatus.SUCCESS)
                .paymentType(EPaymentType.REFUND)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Payment savedPayment = paymentRepo.save(refund);

        // Cập nhật booking
        booking.setRefunded(true);
        booking.setRefundAmount(refundAmount);
        bookingRepo.save(booking);

        // Cập nhật escrow
        escrowTransactionService.updateEscrowStatus(booking.getId(), EscrowStatus.REFUNDED);

        return paymentMapper.toPaymentResponseDTO(savedPayment);
    }

}
