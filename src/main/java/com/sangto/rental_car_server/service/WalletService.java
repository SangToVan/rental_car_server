package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.ETransactionType;

import java.math.BigDecimal;

public interface WalletService {

    Wallet getWallet(Integer userId);

    void creditWallet(Integer userId, BigDecimal amount, ETransactionType transactionType);

    void debitWallet(Integer userId, BigDecimal amount, ETransactionType transactionType);
}
