package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.wallet_transaction.WalletTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.wallet_transaction.WalletTransactionResponseDTO;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.entity.WalletTransaction;
import com.sangto.rental_car_server.domain.mapper.WalletTransactionMapper;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.repository.WalletTransactionRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepo;
    private final WalletTransactionMapper walletTransactionMapper;

    @Override
    public void createTransaction(WalletTransactionRequestDTO requestDTO) {
        WalletTransaction walletTransaction = walletTransactionMapper.toWalletTransaction(requestDTO);
        walletTransactionRepo.save(walletTransaction);
    }

    @Override
    public List<WalletTransactionResponseDTO> getUserTransactionByWalletId(Integer walletId) {
        List<WalletTransaction> walletTransactions = walletTransactionRepo.getTransactionsByWalletId(walletId);

        return walletTransactions.stream().map(walletTransactionMapper::toWalletTransactionResponseDto).toList();
    }

    @Override
    public List<WalletTransactionResponseDTO> getSysTransactionByWalletId(Integer walletId) {
        List<WalletTransaction> walletTransactions = walletTransactionRepo.getTransactionsBySystemWalletId(walletId);

        return walletTransactions.stream().map(walletTransactionMapper::toWalletTransactionResponseDto).toList();
    }
}
