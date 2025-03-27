package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.system_wallet.SystemWalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.SystemWallet;
import com.sangto.rental_car_server.domain.enums.EWalletType;
import com.sangto.rental_car_server.domain.mapper.SystemWalletMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.SystemWalletRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.SystemWalletService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class SystemWalletServiceImpl implements SystemWalletService {

    private final SystemWalletRepository systemWalletRepo;
    private final SystemWalletMapper systemWalletMapper;

    @PostConstruct
    public void init() {
        initializeSystemWallet();
    }

    @Override
    public void initializeSystemWallet() {
        Optional<SystemWallet> findSystemWallet = systemWalletRepo.findFirstByOrderByCreatedAtAsc();
        if (findSystemWallet.isEmpty()) {
            SystemWallet systemWallet = new SystemWallet();
            systemWallet.setServiceFee(BigDecimal.ZERO);
            systemWallet.setRentalFee(BigDecimal.ZERO);
            systemWallet.setRentalFee(BigDecimal.ZERO);
            systemWallet.setCreatedAt(LocalDateTime.now());
            systemWallet.setUpdatedAt(LocalDateTime.now());
            systemWalletRepo.save(systemWallet);
        }
    }

    @Override
    public Response<SystemWalletResponseDTO> getSystemWallet() {
        Optional<SystemWallet> findSystemWallet = systemWalletRepo.findFirstByOrderByCreatedAtAsc();
        if (findSystemWallet.isEmpty()) throw new AppException("Failed to initialize system wallet");
        SystemWallet wallet = findSystemWallet.get();
        return Response.successfulResponse(
                "Get system wallet successfully",
                systemWalletMapper.toSystemWalletResponseDTO(wallet)
        );
    }

    @Override
    public void creditSystemWallet(EWalletType type, BigDecimal amount, String description) {
        Optional<SystemWallet> findSystemWallet = systemWalletRepo.findFirstByOrderByCreatedAtAsc();
        if (findSystemWallet.isEmpty()) throw new AppException("Failed to initialize system wallet");
        SystemWallet wallet = findSystemWallet.get();
        switch (type) {
            case SERVICE_FEE -> wallet.setServiceFee(wallet.getServiceFee().add(amount));
            case RENTAL_FEE -> wallet.setRentalFee(wallet.getRentalFee().add(amount));
            case RESERVE -> wallet.setReserve(wallet.getReserve().add(amount));
        }
        wallet.setUpdatedAt(LocalDateTime.now());
        systemWalletRepo.save(wallet);
    }

    @Override
    public void debitSystemWallet(EWalletType type, BigDecimal amount, String description) {
        Optional<SystemWallet> findSystemWallet = systemWalletRepo.findFirstByOrderByCreatedAtAsc();
        if (findSystemWallet.isEmpty()) throw new AppException("Failed to initialize system wallet");
        SystemWallet wallet = findSystemWallet.get();
        switch (type) {
            case SERVICE_FEE -> wallet.setServiceFee(wallet.getServiceFee().subtract(amount));
            case RENTAL_FEE -> wallet.setRentalFee(wallet.getRentalFee().subtract(amount));
            case RESERVE -> wallet.setReserve(wallet.getReserve().subtract(amount));
        }
        wallet.setUpdatedAt(LocalDateTime.now());
        systemWalletRepo.save(wallet);
    }
}
