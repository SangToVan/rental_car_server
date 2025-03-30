package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.transaction.AddTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import com.sangto.rental_car_server.domain.entity.Transaction;

public interface TransactionMapper {

    Transaction toTransactionEntity(AddTransactionRequestDTO requestDTO);

    TransactionResponseDTO toTransactionResponseDTO(Transaction entity);
}
