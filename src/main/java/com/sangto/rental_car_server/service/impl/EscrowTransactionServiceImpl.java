package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.escrow_transaction.AddEscrowTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.escrow_transaction.EscrowTransactionResponseDTO;
import com.sangto.rental_car_server.domain.dto.transaction.AddTransactionRequestDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.EscrowTransaction;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.entity.Wallet;
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

        // find System wallet
        Optional<User> findSystem = userRepo.findFirstByRole(EUserRole.SYSTEM);
        if (findSystem.isEmpty()) throw new AppException("System not found");
        Wallet systemWallet = findSystem.get().getWallet();

        // credit system wallet
        walletService.creditWallet(systemWallet.getId(), requestDTO.amount());
        AddTransactionRequestDTO systemRequest = AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.CREDIT)
                .walletId(systemWallet.getId())
                .amount(requestDTO.amount())
                .description("Customer payment for booking" + requestDTO.bookingId())
                .build();
        transactionService.addTransaction(systemRequest);

        EscrowTransaction newEscrowTransaction = escrowTransactionMapper.toEscrowTransaction(requestDTO);
        newEscrowTransaction.setBooking(findBooking.get());
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
        List<EscrowTransactionResponseDTO> responseDTOList = list.stream()
                .map(escrowTransactionMapper::toEscrowTransactionResponseDTO).toList();

        return Response.successfulResponse(
                "Get list escrow transaction successfully",
                responseDTOList
        );
    }
}
