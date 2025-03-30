package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByWalletId(Integer walletId);
}
