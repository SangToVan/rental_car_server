package com.sangto.rental_car_server.domain.dto.admin;

import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import lombok.Builder;

import java.math.BigInteger;
import java.util.List;

@Builder
public record DashboardResponseDTO(
        BigInteger amount,
        List<TransactionResponseDTO> list
) {
}
