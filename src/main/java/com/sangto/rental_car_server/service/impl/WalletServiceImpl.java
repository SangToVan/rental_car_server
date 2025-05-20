package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.transaction.AddTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import com.sangto.rental_car_server.domain.dto.wallet.UpdWalletRequestDTO;
import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.ETransactionType;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.domain.mapper.TransactionMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.TransactionRepository;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepo;
    private final UserRepository userRepo;
    private final TransactionRepository transactionRepo;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Override
    public Response<WalletResponseDTO> getWalletDetail(Integer userId) {
        Optional<Wallet> findWallet = userRepo.findWalletById(userId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet wallet = findWallet.get();

        List<TransactionResponseDTO> findList = transactionRepo.findRecentTransactionsByWallet(wallet.getId(),
                LocalDateTime.now().minusMonths(3L)).stream().map(transactionMapper::toTransactionResponseDTO).toList();
        WalletResponseDTO responseDTO = WalletResponseDTO.builder()
                .balance(wallet.getBalance().toString())
                .transactionList(findList)
                .build();
        return Response.successfulResponse(
                "Get wallet successfully", responseDTO
        );
    }

    @Override
    @Transactional
    public Response<String> updateWallet(Integer userId, UpdWalletRequestDTO updWalletRequestDTO) {
        Optional<Wallet> findWallet = userRepo.findWalletById(userId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet wallet = findWallet.get();

        if (updWalletRequestDTO.type().equals(ETransactionType.TOP_UP)) {
            creditWallet(wallet.getId(), new BigDecimal(updWalletRequestDTO.amount()));
            transactionService.addTransaction(AddTransactionRequestDTO.builder()
                            .transactionType(ETransactionType.CREDIT)
                            .amount(new BigDecimal(updWalletRequestDTO.amount()))
                            .description("Top up transaction")
                            .walletId(wallet.getId())
                    .build());
        } else if (updWalletRequestDTO.type().equals(ETransactionType.WITHDRAW)) {
            debitWallet(wallet.getId(), new BigDecimal(updWalletRequestDTO.amount()));
            transactionService.addTransaction(AddTransactionRequestDTO.builder()
                    .transactionType(ETransactionType.DEBIT)
                    .amount(new BigDecimal(updWalletRequestDTO.amount()))
                    .description("Withdraw transaction")
                    .walletId(wallet.getId())
                    .build());
        } else throw new AppException("Wrong transaction type");

        return Response.successfulResponse(
                "Update wallet successfully"
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

        try {
            debitWallet(customerWallet.getId(), amount);
        } catch (AppException e) {
            throw new AppException(e.getMessage());
        }

        AddTransactionRequestDTO customerRequest = AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.DEBIT)
                .walletId(customerWallet.getId())
                .amount(amount)
                .description("Payment for booking" + bookingId)
                .build();

        transactionService.addTransaction(customerRequest);

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
    public Response<String> releaseInsurance(BigDecimal insurance, Integer bookingId) {

        Optional<User> findAdmin = userRepo.findAdmin(EUserRole.ADMIN);
        if (findAdmin.isEmpty()) throw new AppException("Admin not found");
        Wallet adminWallet = findAdmin.get().getWallet();

        Optional<User> findSystem = userRepo.findFirstByRole(EUserRole.SYSTEM);
        if (findSystem.isEmpty()) throw new AppException("System not found");
        Wallet systemWallet = findSystem.get().getWallet();

        try {
            debitWallet(systemWallet.getId(), insurance);
            creditWallet(adminWallet.getId(), insurance);
        } catch (AppException e) {
            throw new AppException(e.getMessage());
        }

        AddTransactionRequestDTO adminRequest = AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.CREDIT)
                .walletId(adminWallet.getId())
                .amount(insurance)
                .description("Insurance release for booking" + bookingId)
                .build();

        AddTransactionRequestDTO systemRequest = AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.DEBIT)
                .walletId(systemWallet.getId())
                .amount(insurance)
                .description("Release insurance for booking" + bookingId)
                .build();

        transactionService.addTransaction(adminRequest);
        transactionService.addTransaction(systemRequest);

        return Response.successfulResponse("Release booking insurance successfully");
    }

    @Override
    @Transactional
    public Response<String> releaseSystemFee(Integer ownerId, BigDecimal systemFee, Integer bookingId) {
        Optional<User> findAdmin = userRepo.findAdmin(EUserRole.ADMIN);
        if (findAdmin.isEmpty()) throw new AppException("Admin not found");
        Wallet adminWallet = findAdmin.get().getWallet();

        Optional<Wallet> findWallet = userRepo.findWalletById(ownerId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet ownerWallet = findWallet.get();

        try {
            debitWallet(ownerWallet.getId(), systemFee);
            creditWallet(adminWallet.getId(), systemFee);
        } catch (AppException e) {
            throw new AppException(e.getMessage());
        }

        AddTransactionRequestDTO adminRequest = AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.CREDIT)
                .walletId(adminWallet.getId())
                .amount(systemFee)
                .description("System fee release for booking" + bookingId)
                .build();

        AddTransactionRequestDTO ownerRequest = AddTransactionRequestDTO.builder()
                .transactionType(ETransactionType.DEBIT)
                .walletId(ownerWallet.getId())
                .amount(systemFee)
                .description("Release system fee for booking" + bookingId)
                .build();

        transactionService.addTransaction(adminRequest);
        transactionService.addTransaction(ownerRequest);

        return Response.successfulResponse("Release booking system fee successfully");
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
