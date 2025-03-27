package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.system_wallet.SystemWalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.SystemWallet;
import com.sangto.rental_car_server.domain.mapper.SystemWalletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemWalletMapperImpl implements SystemWalletMapper {
    @Override
    public SystemWalletResponseDTO toSystemWalletResponseDTO(SystemWallet entity) {
        return SystemWalletResponseDTO.builder()
                .service_fee(entity.getServiceFee().toString())
                .rental_fee(entity.getRentalFee().toString())
                .reserve(entity.getReserve().toString())
                .total(entity.getTotalBalance().toString())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
