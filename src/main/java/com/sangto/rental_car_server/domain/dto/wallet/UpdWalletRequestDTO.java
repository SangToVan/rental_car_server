package com.sangto.rental_car_server.domain.dto.wallet;

import com.sangto.rental_car_server.domain.enums.ETransactionType;
import lombok.Builder;

@Builder
public record UpdWalletRequestDTO(
        ETransactionType type,
        String amount
) {
}
