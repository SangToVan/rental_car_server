package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.Wallet;

public interface WalletMapper {

    WalletResponseDTO toWalletResponseDTO(Wallet wallet);
}
