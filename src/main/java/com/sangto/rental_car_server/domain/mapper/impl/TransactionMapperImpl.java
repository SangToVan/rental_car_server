package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.transaction.AddTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import com.sangto.rental_car_server.domain.entity.Transaction;
import com.sangto.rental_car_server.domain.mapper.TransactionMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public Transaction toTransactionEntity(AddTransactionRequestDTO requestDTO) {
        return Transaction.builder()
                .transactionType(requestDTO.transactionType())
                .amount(new BigDecimal(requestDTO.amount()))
                .description(requestDTO.description())
                .transactionDate(LocalDateTime.now())
                .build();
    }

    @Override
    public TransactionResponseDTO toTransactionResponseDTO(Transaction entity) {
        return TransactionResponseDTO.builder()
                .transactionType(entity.getTransactionType())
                .amount(entity.getAmount().toString())
                .description(entity.getDescription())
                .walletId(entity.getWallet().getId())
                .transactionDate(entity.getTransactionDate().toString())
                .build();
    }
}
