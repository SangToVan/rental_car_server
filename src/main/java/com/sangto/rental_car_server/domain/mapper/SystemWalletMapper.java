package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.system_wallet.SystemWalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.SystemWallet;

public interface SystemWalletMapper {

    SystemWalletResponseDTO toSystemWalletResponseDTO(SystemWallet entity);
}
