package com.sangto.rental_car_server.domain.dto.wallet;

import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record WalletResponseDTO(
        String balance,
        List<TransactionResponseDTO> transactionList
) {
}
