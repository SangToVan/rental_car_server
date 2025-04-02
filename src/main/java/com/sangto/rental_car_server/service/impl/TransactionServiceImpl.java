package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.transaction.AddTransactionRequestDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import com.sangto.rental_car_server.domain.entity.Transaction;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.mapper.TransactionMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.TransactionRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepo;
    private final TransactionRepository transactionRepo;
    private final TransactionMapper transactionMapper;

    @Override
    public Response<List<TransactionResponseDTO>> getListByUserId(Integer userId) {

        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("This user does not exist");
        User user = findUser.get();

        Optional<Wallet> findWallet = userRepo.findWalletById(user.getId());
        if (findWallet.isEmpty()) throw new AppException("This wallet does not exist");
        Wallet wallet = findWallet.get();

        List<Transaction> list = transactionRepo.findAllByWalletId(wallet.getId());
        List<TransactionResponseDTO> li = list.stream().map(transactionMapper :: toTransactionResponseDTO).toList();

        return Response.successfulResponse(
                "Get list transaction success",
                li
        );
    }

    @Override
    @Transactional
    public Transaction addTransaction(AddTransactionRequestDTO requestDTO) {
        Optional<Wallet> findWallet = userRepo.findWalletById(requestDTO.walletId());
        if (findWallet.isEmpty()) throw new AppException("This wallet does not exist");
        Wallet wallet = findWallet.get();
        try {
            Transaction newTransaction = transactionMapper.toTransactionEntity(requestDTO);
            newTransaction.setWallet(wallet);
            transactionRepo.save(newTransaction);
            return newTransaction;
        } catch (Exception e) {
            throw new AppException("Add new transaction unsuccessfully" + e.getMessage());
        }
    }
}
