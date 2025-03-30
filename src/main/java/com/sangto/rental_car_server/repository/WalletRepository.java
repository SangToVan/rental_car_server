package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    @Query("SELECT w FROM Wallet w WHERE w.id = :walletId")
    Optional<Wallet> findByWalletId(@Param("walletId") Integer walletId);
}
