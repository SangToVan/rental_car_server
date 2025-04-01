package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.escrow_transaction.AddEscrowTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.escrow_transaction.EscrowTransactionResponseDTO;
import com.sangto.rental_car_server.domain.entity.EscrowTransaction;

public interface EscrowTransactionMapper {

    EscrowTransaction toEscrowTransaction(AddEscrowTransactionRequestDTO requestDTO);

    EscrowTransactionResponseDTO toEscrowTransactionResponseDTO(EscrowTransaction entity);
}
