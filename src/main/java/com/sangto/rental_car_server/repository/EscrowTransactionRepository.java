package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.EscrowTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EscrowTransactionRepository extends JpaRepository<EscrowTransaction, Integer> {

    Optional<EscrowTransaction> findByBookingId(Integer bookingId);
}
