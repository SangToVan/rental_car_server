package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findAllByUserId(Integer userId);
}
