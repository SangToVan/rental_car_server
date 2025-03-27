package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepo;

    @Override
    public Wallet getWallet(Integer userId) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User not found");
        Optional<Wallet> findWallet = userRepo.findWalletById(userId);
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        return findWallet.get();
    }

    @Override
    public void updateWallet(Integer userId, BigDecimal amount) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User not found");
        User user = findUser.get();
        Wallet wallet = user.getWallet();
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdated_at(new Date());

        userRepo.save(user);
    }
}
