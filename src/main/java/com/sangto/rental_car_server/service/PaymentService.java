package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.payment.AddPaymentRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.responses.Response;

import java.util.List;

public interface PaymentService {

    Response<List<PaymentResponseDTO>> getListByUserId(Integer userId);

    Payment addPayment(AddPaymentRequestDTO requestDTO);
}
