package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.payment.AddPaymentRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.entity.Payment;

public interface PaymentMapper {

    Payment addPaymentRequestDTOtoEntity(AddPaymentRequestDTO requestDTO);

    PaymentResponseDTO toPaymentResponseDTO(Payment entity);
}
