package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.SystemWallet;
import com.sangto.rental_car_server.domain.enums.EWalletType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemWalletRepository extends JpaRepository<SystemWallet, Integer> {

    Optional<SystemWallet> findFirstByOrderByCreatedAtAsc();
}
