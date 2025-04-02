package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.escrow_transaction.AddEscrowTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.escrow_transaction.EscrowTransactionResponseDTO;
import com.sangto.rental_car_server.domain.dto.transaction.AddTransactionRequestDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.EscrowTransaction;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.ETransactionType;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.domain.enums.EscrowStatus;
import com.sangto.rental_car_server.domain.mapper.EscrowTransactionMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.EscrowTransactionRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.EscrowTransactionService;
import com.sangto.rental_car_server.service.TransactionService;
import com.sangto.rental_car_server.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class EscrowTransactionServiceImpl implements EscrowTransactionService {

    private final EscrowTransactionRepository escrowTransactionRepo;
    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final EscrowTransactionMapper escrowTransactionMapper;

    @Override
    public Response<EscrowTransactionResponseDTO> addEscrowTransaction(AddEscrowTransactionRequestDTO requestDTO) {

        Optional<Booking> findBooking = bookingRepo.findById(requestDTO.bookingId());
        if (findBooking.isEmpty()) throw new AppException("Booking not found");
        Booking booking = findBooking.get();

        // calculate rental fee
        Optional<User> findCustomer = userRepo.findById(booking.getUser().getId());
        if (findCustomer.isEmpty()) throw new AppException("Customer not found");
        User customer = findCustomer.get();

        Optional<User> findAdmin = userRepo.findAdmin(EUserRole.ADMIN);
        if (findAdmin.isEmpty()) throw new AppException("Admin not found");
        User admin = findAdmin.get();

        walletService.debitWallet(customer.getWallet().getId(), booking.getTotalPrice());
        transactionService.addTransaction(AddTransactionRequestDTO.builder()
                        .transactionType(ETransactionType.PAYMENT)
                        .amount(booking.getTotalPrice().toString())
                        .description("Payment for booking " + booking.getId())
                        .walletId(customer.getWallet().getId())
                .build());

        walletService.creditWallet(admin.getWallet().getId(), booking.getTotalPrice());
        transactionService.addTransaction(AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.PAYMENT)
                .amount(booking.getTotalPrice().toString())
                .description("Escrow payment for booking " + booking.getId())
                .walletId(admin.getWallet().getId())
                .build());

        EscrowTransaction newEscrowTransaction = escrowTransactionMapper.toEscrowTransaction(requestDTO);
        newEscrowTransaction.setCreatedAt(LocalDateTime.now());
        newEscrowTransaction.setUpdatedAt(LocalDateTime.now());
        escrowTransactionRepo.save(newEscrowTransaction);

        return Response.successfulResponse(
                "Add escrow transaction successfully",
                escrowTransactionMapper.toEscrowTransactionResponseDTO(newEscrowTransaction)
        );
    }

    @Override
    @Transactional
    public Response<EscrowTransactionResponseDTO> updateEscrowStatus(Integer bookingId, EscrowStatus status) {

        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("Booking not found");
        Optional<EscrowTransaction> findEscrowTransaction = escrowTransactionRepo.findByBookingId(bookingId);
        if (findEscrowTransaction.isEmpty()) throw new AppException("Escrow transaction not found");
        EscrowTransaction escrowTransaction = findEscrowTransaction.get();

        if (status.equals(EscrowStatus.REFUNDED)) {
            refundEscrow(findBooking.get());
        } else if (status.equals(EscrowStatus.RELEASED)) {
            releaseEscrow(findBooking.get());
        } else {
            throw new AppException("Cannot update escrow status " + status);
        }

        escrowTransaction.setStatus(status);
        escrowTransaction.setUpdatedAt(LocalDateTime.now());
        escrowTransactionRepo.save(escrowTransaction);

        return Response.successfulResponse(
                "Update status escrow transaction successfully",
                escrowTransactionMapper.toEscrowTransactionResponseDTO(escrowTransaction)
        );
    }

    @Override
    public Response<List<EscrowTransactionResponseDTO>> getAllEscrowTransactions() {

        List<EscrowTransaction> list = escrowTransactionRepo.findAll();
        List<EscrowTransactionResponseDTO> responseDTOList = list.stream().map(escrowTransactionMapper::toEscrowTransactionResponseDTO).toList();

        return Response.successfulResponse(
                "Get list escrow transaction successfully",
                responseDTOList
        );
    }
    @Transactional
    public void refundEscrow(Booking booking) {
        // calculate rental fee
        Optional<User> findCustomer = userRepo.findById(booking.getUser().getId());
        if (findCustomer.isEmpty()) throw new AppException("Customer not found");
        User customer = findCustomer.get();

        Optional<User> findAdmin = userRepo.findAdmin(EUserRole.ADMIN);
        if (findAdmin.isEmpty()) throw new AppException("Admin not found");
        User admin = findAdmin.get();

        walletService.creditWallet(customer.getWallet().getId(), booking.getTotalPrice());
        transactionService.addTransaction(AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.REFUND)
                .amount(booking.getTotalPrice().toString())
                .description("Refund for booking " + booking.getId())
                .walletId(customer.getWallet().getId())
                .build());

        walletService.debitWallet(admin.getWallet().getId(), booking.getTotalPrice());
        transactionService.addTransaction(AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.REFUND)
                .amount(booking.getTotalPrice().toString())
                .description("Escrow refund for booking " + booking.getId())
                .walletId(admin.getWallet().getId())
                .build());
    }

    @Transactional
    public void releaseEscrow(Booking booking) {
        // calculate rental fee
        Optional<User> findOwner = userRepo.findById(booking.getCar().getCarOwner().getId());
        if (findOwner.isEmpty()) throw new AppException("Owner not found");
        User owner = findOwner.get();

        Optional<User> findAdmin = userRepo.findAdmin(EUserRole.ADMIN);
        if (findAdmin.isEmpty()) throw new AppException("Admin not found");
        User admin = findAdmin.get();

        walletService.creditWallet(owner.getWallet().getId(), booking.getTotalPrice());
        transactionService.addTransaction(AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.RELEASE)
                .amount(booking.getTotalPrice().toString())
                .description("Release for booking " + booking.getId())
                .walletId(owner.getWallet().getId())
                .build());

        walletService.debitWallet(admin.getWallet().getId(), booking.getTotalPrice());
        transactionService.addTransaction(AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.RELEASE)
                .amount(booking.getTotalPrice().toString())
                .description("Escrow release for booking " + booking.getId())
                .walletId(admin.getWallet().getId())
                .build());
    }
}
