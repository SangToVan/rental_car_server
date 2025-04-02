package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.domain.mapper.WalletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletMapperImpl implements WalletMapper {

    private final UserMapper userMapper;

    @Override
    public WalletResponseDTO toWalletResponseDTO(Wallet wallet) {
        return WalletResponseDTO.builder()
                .info(userMapper.toUserResponseDTO(wallet.getUser()))
                .balance(wallet.getBalance().toString())
                .updatedAt(wallet.getUpdatedAt())
                .build();
    }
}
