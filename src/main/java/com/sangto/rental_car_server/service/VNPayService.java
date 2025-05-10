package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.entity.Payment;
import jakarta.servlet.http.HttpServletRequest;

import java.net.UnknownHostException;
import java.util.Map;

public interface VNPayService {
    String createPaymentUrl(Payment payment, HttpServletRequest request) throws UnknownHostException;
    boolean validateSignature(Map<String, String> vnpParams, String receivedHash);
}
