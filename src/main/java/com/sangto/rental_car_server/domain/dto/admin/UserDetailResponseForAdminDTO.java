package com.sangto.rental_car_server.domain.dto.admin;

import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import lombok.Builder;

@Builder
public record UserDetailResponseForAdminDTO(
        UserDetailResponseDTO userInfo,
        WalletResponseDTO wallet
) {
}
