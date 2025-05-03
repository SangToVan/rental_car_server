package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.entity.Payment;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface VNPayService {
    String createPaymentUrl(Payment payment, HttpServletRequest request);

    boolean checkPayment(Payment payment, Map<String, String> params);
}
