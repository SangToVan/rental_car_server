package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.wallet_transaction.WalletTransactionRequestDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.ETransactionType;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.service.WalletService;
import com.sangto.rental_car_server.service.WalletTransactionService;
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

    private final UserRepository userRepo;
    private final WalletTransactionService walletTransactionService;

    @Override
    public Wallet getWallet(Integer userId) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User not found");
        Optional<Wallet> findWallet = userRepo.findWalletById(userId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        return findWallet.get();
    }

    @Override
    @Transactional
    public void creditWallet(Integer userId, BigDecimal amount, ETransactionType transactionType) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User not found");
        User user = findUser.get();
        Wallet wallet = user.getWallet();
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedAt(LocalDateTime.now());

        walletTransactionService.createTransaction(WalletTransactionRequestDTO.builder()
                .amount(amount)
                .transactionType(transactionType)
                .description(null)
                .createdAt(wallet.getUpdatedAt())
                .build());

        userRepo.save(user);
    }

    @Override
    @Transactional
    public void debitWallet(Integer userId, BigDecimal amount, ETransactionType transactionType) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User not found");
        User user = findUser.get();
        Wallet wallet = user.getWallet();
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setUpdatedAt(LocalDateTime.now());

        walletTransactionService.createTransaction(WalletTransactionRequestDTO.builder()
                .amount(amount)
                .transactionType(transactionType)
                .description(null)
                .createdAt(wallet.getUpdatedAt())
                .build());

        userRepo.save(user);
    }

}
