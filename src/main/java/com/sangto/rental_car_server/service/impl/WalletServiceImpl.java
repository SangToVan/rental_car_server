package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.mapper.WalletMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.WalletRepository;
import com.sangto.rental_car_server.responses.Response;
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
    private final WalletMapper walletMapper;

    @Override
    public Response<WalletResponseDTO> getWalletDetail(Integer walletId) {
        Optional<Wallet> findWallet = walletRepo.findById(walletId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet wallet = findWallet.get();

        return Response.successfulResponse(
                "Get wallet successfully",
                walletMapper.toWalletResponseDTO(wallet)
        );
    }

    @Override
    @Transactional
    public Response<String> creditWallet(Integer walletId, BigDecimal amount) {
        Optional<Wallet> findWallet = walletRepo.findById(walletId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet wallet = findWallet.get();
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(wallet);

        return Response.successfulResponse("Credit wallet successfully");
    }

    @Override
    @Transactional
    public Response<String> debitWallet(Integer walletId, BigDecimal amount) {
        Optional<Wallet> findWallet = walletRepo.findById(walletId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet wallet = findWallet.get();
        if (wallet.getBalance().compareTo(amount) < 0) throw new AppException("Insufficient balance");
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(wallet);

        return Response.successfulResponse("Debit wallet successfully");
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
}
