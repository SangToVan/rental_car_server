package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.transaction.AddTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import com.sangto.rental_car_server.domain.entity.Transaction;
import com.sangto.rental_car_server.responses.Response;

import java.util.List;

public interface TransactionService {

    Response<List<TransactionResponseDTO>> getListByUserId(Integer userId);

    Transaction addTransaction(AddTransactionRequestDTO requestDTO);
}
