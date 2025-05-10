package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findAllByUserId(Integer userId);

    Optional<Payment> findByBookingId(Integer bookingId);

    Optional<Payment> findByTransactionCode(String transactionCode);

    @Query("SELECT p FROM Payment p JOIN FETCH p.booking WHERE p.transactionCode = :txnRef")
    Optional<Payment> findByTransactionCodeWithBooking(@Param("txnRef") String txnRef);

}
