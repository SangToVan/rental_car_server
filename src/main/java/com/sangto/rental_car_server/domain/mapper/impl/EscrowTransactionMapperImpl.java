package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.escrow_transaction.AddEscrowTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.escrow_transaction.EscrowTransactionResponseDTO;
import com.sangto.rental_car_server.domain.entity.EscrowTransaction;
import com.sangto.rental_car_server.domain.mapper.EscrowTransactionMapper;
import org.springframework.stereotype.Component;

@Component
public class EscrowTransactionMapperImpl implements EscrowTransactionMapper {

    @Override
    public EscrowTransaction toEscrowTransaction(AddEscrowTransactionRequestDTO requestDTO) {
        return EscrowTransaction.builder()
                .amount(requestDTO.amount())
                .status(requestDTO.status())
                .build();
    }

    @Override
    public EscrowTransactionResponseDTO toEscrowTransactionResponseDTO(EscrowTransaction entity) {
        return EscrowTransactionResponseDTO.builder()
                .bookingId(entity.getBooking().getId())
                .amount(entity.getAmount().toString())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt().toString())
                .updatedAt(entity.getUpdatedAt().toString())
                .build();
    }
}
