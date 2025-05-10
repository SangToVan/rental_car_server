package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.transaction.AddTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.wallet.UpdWalletDTO;
import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.ETransactionType;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.domain.mapper.WalletMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.repository.WalletRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.TransactionService;
import com.sangto.rental_car_server.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepo;
    private final UserRepository userRepo;
    private final TransactionService transactionService;
    private final WalletMapper walletMapper;

    @Override
    public Response<WalletResponseDTO> getWalletDetail(Integer userId) {
        Optional<Wallet> findWallet = userRepo.findWalletById(userId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet wallet = findWallet.get();

        return Response.successfulResponse(
                "Get wallet successfully",
                walletMapper.toWalletResponseDTO(wallet)
        );
    }

    @Override
    @Transactional
    public Response<WalletResponseDTO> updateWallet(Integer userId, UpdWalletDTO updWalletDTO) {
        Optional<Wallet> findWallet = userRepo.findWalletById(userId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet wallet = findWallet.get();

        if (updWalletDTO.type().equals(ETransactionType.TOP_UP)) {
            creditWallet(wallet.getId(), new BigDecimal(updWalletDTO.amount()));
            transactionService.addTransaction(AddTransactionRequestDTO.builder()
                            .transactionType(ETransactionType.CREDIT)
                            .amount(new BigDecimal(updWalletDTO.amount()))
                            .description("Top up transaction")
                            .walletId(wallet.getId())
                    .build());
        } else if (updWalletDTO.type().equals(ETransactionType.WITHDRAW)) {
            debitWallet(wallet.getId(), new BigDecimal(updWalletDTO.amount()));
            transactionService.addTransaction(AddTransactionRequestDTO.builder()
                    .transactionType(ETransactionType.DEBIT)
                    .amount(new BigDecimal(updWalletDTO.amount()))
                    .description("Withdraw transaction")
                    .walletId(wallet.getId())
                    .build());
        } else throw new AppException("Wrong transaction type");

        return Response.successfulResponse(
                "Update wallet successfully",
                walletMapper.toWalletResponseDTO(wallet)
        );
    }

    @Override
    @Transactional
    public void creditWallet(Integer walletId, BigDecimal amount) {
        Optional<Wallet> findWallet = walletRepo.findById(walletId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet wallet = findWallet.get();
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(wallet);

        Response.successfulResponse("Credit wallet successfully");
    }

    @Override
    @Transactional
    public void debitWallet(Integer walletId, BigDecimal amount) {
        Optional<Wallet> findWallet = walletRepo.findById(walletId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet wallet = findWallet.get();
        if (wallet.getBalance().compareTo(amount) < 0) throw new AppException("Insufficient balance");
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(wallet);

        Response.successfulResponse("Debit wallet successfully");
    }

    @Override
    @Transactional
    public Response<String> transferWallet(Integer fromWalletId, Integer toWalletId, BigDecimal amount) {
        Optional<Wallet> findFromWallet = walletRepo.findById(fromWalletId);
        if (findFromWallet.isEmpty()) throw new AppException("From Wallet not found");
        Wallet fromWallet = findFromWallet.get();

        Optional<Wallet> findToWallet = walletRepo.findById(toWalletId);
        if (findToWallet.isEmpty()) throw new AppException("To Wallet not found");
        Wallet toWallet = findToWallet.get();

        if (fromWallet.getBalance().compareTo(amount) < 0) throw new AppException("Insufficient balance");
        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        fromWallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(fromWallet);

        toWallet.setBalance(toWallet.getBalance().add(amount));
        toWallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(toWallet);

        return Response.successfulResponse("Transfer wallet successfully");
    }

    @Override
    @Transactional
    public Response<String> paymentBooking(Integer customerId, BigDecimal amount, Integer bookingId) {

        Optional<Wallet> findWallet = userRepo.findWalletById(customerId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet customerWallet = findWallet.get();

        Optional<User> findSystem = userRepo.findFirstByRole(EUserRole.SYSTEM);
        if (findSystem.isEmpty()) throw new AppException("System not found");
        Wallet systemWallet = findSystem.get().getWallet();

        try {
            debitWallet(customerWallet.getId(), amount);
            creditWallet(systemWallet.getId(), amount);
        } catch (AppException e) {
            throw new AppException(e.getMessage());
        }

        AddTransactionRequestDTO customerRequest = AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.DEBIT)
                .walletId(customerWallet.getId())
                .amount(amount)
                .description("Payment for booking" + bookingId)
                .build();

        AddTransactionRequestDTO systemRequest = AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.CREDIT)
                .walletId(systemWallet.getId())
                .amount(amount)
                .description("Customer payment for booking" + bookingId)
                .build();

        transactionService.addTransaction(customerRequest);
        transactionService.addTransaction(systemRequest);

        return Response.successfulResponse("Payment booking successfully");
    }

    @Override
    @Transactional
    public Response<String> releaseBooking(Integer ownerId, BigDecimal amount, Integer bookingId) {
        Optional<Wallet> findWallet = userRepo.findWalletById(ownerId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet ownerWallet = findWallet.get();

        Optional<User> findSystem = userRepo.findFirstByRole(EUserRole.SYSTEM);
        if (findSystem.isEmpty()) throw new AppException("System not found");
        Wallet systemWallet = findSystem.get().getWallet();

        try {
            debitWallet(systemWallet.getId(), amount);
            creditWallet(ownerWallet.getId(), amount);
        } catch (AppException e) {
            throw new AppException(e.getMessage());
        }

        AddTransactionRequestDTO ownerRequest = AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.CREDIT)
                .walletId(ownerWallet.getId())
                .amount(amount)
                .description("Payment release for booking" + bookingId)
                .build();

        AddTransactionRequestDTO systemRequest = AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.DEBIT)
                .walletId(systemWallet.getId())
                .amount(amount)
                .description("Release payment for booking" + bookingId)
                .build();

        transactionService.addTransaction(ownerRequest);
        transactionService.addTransaction(systemRequest);

        return Response.successfulResponse("Release booking payment successfully");
    }

    @Override
    @Transactional
    public Response<String> refundBooking(Integer customerId, BigDecimal amount, Integer bookingId) {
        Optional<Wallet> findWallet = userRepo.findWalletById(customerId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet customerWallet = findWallet.get();

        Optional<User> findSystem = userRepo.findFirstByRole(EUserRole.SYSTEM);
        if (findSystem.isEmpty()) throw new AppException("System not found");
        Wallet systemWallet = findSystem.get().getWallet();

        try {
            creditWallet(customerWallet.getId(), amount);
            debitWallet(systemWallet.getId(), amount);
        } catch (AppException e) {
            throw new AppException(e.getMessage());
        }

        AddTransactionRequestDTO customerRequest = AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.CREDIT)
                .walletId(customerWallet.getId())
                .amount(amount)
                .description("Payment refund for booking" + bookingId)
                .build();

        AddTransactionRequestDTO systemRequest = AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.DEBIT)
                .walletId(systemWallet.getId())
                .amount(amount)
                .description("Refund payment for booking" + bookingId)
                .build();

        transactionService.addTransaction(customerRequest);
        transactionService.addTransaction(systemRequest);

        return Response.successfulResponse("Refund booking payment successfully");
    }
}
