package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.wallet_transaction.WalletTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.wallet_transaction.WalletTransactionResponseDTO;
import com.sangto.rental_car_server.domain.entity.WalletTransaction;
import com.sangto.rental_car_server.domain.mapper.WalletTransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletTransactionMapperImpl implements WalletTransactionMapper {
    @Override
    public WalletTransactionResponseDTO toWalletTransactionResponseDto(WalletTransaction entity) {
        return WalletTransactionResponseDTO.builder()
                .amount(entity.getAmount().toString())
                .transactionType(entity.getTransactionType())
                .description(entity.getDescription())
                .createdAt(entity.getTransactionDate())
                .build();
    }

    @Override
    public WalletTransaction toWalletTransaction(WalletTransactionRequestDTO requestDTO) {
        return WalletTransaction.builder()
                .amount(requestDTO.amount())
                .transactionType(requestDTO.transactionType())
                .description(requestDTO.description())
                .createdAt(requestDTO.createdAt())
                .build();
    }
}
