package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.escrow_transaction.AddEscrowTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.escrow_transaction.EscrowTransactionResponseDTO;
import com.sangto.rental_car_server.domain.enums.EscrowStatus;
import com.sangto.rental_car_server.responses.Response;

import java.util.List;

public interface EscrowTransactionService {

    Response<EscrowTransactionResponseDTO> addEscrowTransaction(AddEscrowTransactionRequestDTO requestDTO);

    Response<EscrowTransactionResponseDTO> updateEscrowStatus(Integer bookingId, EscrowStatus status);

    Response<List<EscrowTransactionResponseDTO>> getAllEscrowTransactions();
}
