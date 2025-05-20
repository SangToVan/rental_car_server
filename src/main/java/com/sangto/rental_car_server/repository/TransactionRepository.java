package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByWalletId(Integer walletId);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.wallet.id = :walletId " +
            "AND t.transactionDate >= :threeMonthsAgo " +
            "ORDER BY t.transactionDate DESC")
    List<Transaction> findRecentTransactionsByWallet(
            @Param("walletId") Integer walletId,
            @Param("threeMonthsAgo") LocalDateTime threeMonthsAgo
    );

}
