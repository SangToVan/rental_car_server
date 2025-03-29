package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.WalletTransaction;
import com.sangto.rental_car_server.domain.enums.ETransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Integer> {

    @Query("SELECT wt FROM WalletTransaction wt WHERE wt.wallet.id = :walletId")
    List<WalletTransaction> findByWalletId(@Param("walletId") Integer walletId);

    @Query("SELECT wt FROM WalletTransaction wt WHERE wt.transactionType = :transactionType")
    List<WalletTransaction> findByTransactionType(@Param("transactionType") ETransactionType transactionType);
}
