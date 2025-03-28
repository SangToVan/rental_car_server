package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Integer> {

    // Lấy danh sách giao dịch theo wallet_id
    List<WalletTransaction> findByWalletId(Integer walletId);

    // Lấy danh sách giao dịch theo system_wallet_id
    List<WalletTransaction> findBySystemWalletId(Integer systemWalletId);

    // Sử dụng @Query để truy vấn tùy chỉnh nếu cần
    @Query("SELECT wt FROM WalletTransaction wt WHERE wt.wallet.id = :walletId")
    List<WalletTransaction> getTransactionsByWalletId(@Param("walletId") Integer walletId);

    @Query("SELECT wt FROM WalletTransaction wt WHERE wt.systemWallet.id = :systemWalletId")
    List<WalletTransaction> getTransactionsBySystemWalletId(@Param("systemWalletId") Integer systemWalletId);
}
