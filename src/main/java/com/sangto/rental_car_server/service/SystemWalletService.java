package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.system_wallet.SystemWalletResponseDTO;
import com.sangto.rental_car_server.domain.enums.EWalletType;
import com.sangto.rental_car_server.responses.Response;

import java.math.BigDecimal;

public interface SystemWalletService {

    void initializeSystemWallet();

    Response<SystemWalletResponseDTO> getSystemWallet();

    void creditSystemWallet(EWalletType type, BigDecimal amount, String description);

    void debitSystemWallet(EWalletType type, BigDecimal amount, String description);
}
