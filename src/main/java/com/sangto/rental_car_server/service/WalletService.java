package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.entity.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    Wallet getWallet(Integer userId);

    void updateWallet(Integer userId, BigDecimal amount);
}
