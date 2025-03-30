package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Transaction;
import com.sangto.rental_car_server.domain.enums.ETransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT wt FROM Transaction wt WHERE wt.wallet.id = :walletId")
    List<Transaction> findByWalletId(@Param("walletId") Integer walletId);

    @Query("SELECT wt FROM Transaction wt WHERE wt.transactionType = :transactionType")
    List<Transaction> findByTransactionType(@Param("transactionType") ETransactionType transactionType);
}
