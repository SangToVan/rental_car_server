package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.ETransactionType;
import com.sangto.rental_car_server.responses.Response;

import java.math.BigDecimal;

public interface WalletService {

    Response<WalletResponseDTO> getWalletDetail(Integer walletId);

    Response<String> creditWallet(Integer walletId, BigDecimal amount);

    Response<String> debitWallet(Integer walletId, BigDecimal amount);

    Response<String> transferWallet(Integer fromWalletId, Integer toWalletId, BigDecimal amount);
}
