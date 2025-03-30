package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.wallet_transaction.WalletTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.wallet_transaction.WalletTransactionResponseDTO;
import com.sangto.rental_car_server.domain.entity.Transaction;

public interface WalletTransactionMapper {
    WalletTransactionResponseDTO toWalletTransactionResponseDto(Transaction entity);

    Transaction toWalletTransaction(WalletTransactionRequestDTO requestDTO);
}
