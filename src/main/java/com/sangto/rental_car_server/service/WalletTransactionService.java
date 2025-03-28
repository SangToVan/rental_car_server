package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.wallet_transaction.WalletTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.wallet_transaction.WalletTransactionResponseDTO;
import com.sangto.rental_car_server.responses.Response;

import java.util.List;

public interface WalletTransactionService {

    void createTransaction(WalletTransactionRequestDTO requestDTO);

    List<WalletTransactionResponseDTO> getUserTransactionByWalletId(Integer walletId);

    List<WalletTransactionResponseDTO> getSysTransactionByWalletId(Integer walletId);
}
