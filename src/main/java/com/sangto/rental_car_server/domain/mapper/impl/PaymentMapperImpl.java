package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.payment.AddPaymentRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.domain.mapper.PaymentMapper;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapperImpl implements PaymentMapper {
    @Override
    public Payment addPaymentRequestDTOtoEntity(AddPaymentRequestDTO requestDTO) {
        return Payment.builder()
                .paymentMethod(requestDTO.paymentMethod())
                .paymentType(requestDTO.paymentType())
                .build();
    }

    @Override
    public PaymentResponseDTO toPaymentResponseDTO(Payment entity) {
        return PaymentResponseDTO.builder()
                .paymentId(entity.getId())
                .amount(entity.getAmount().toString())
                .paymentMethod(entity.getPaymentMethod())
                .paymentStatus(entity.getPaymentStatus())
                .paymentType(entity.getPaymentType())
                .transactionCode(entity.getTransactionCode())
                .paymentUrl("")
                .createdAt(entity.getCreatedAt().toString())
                .updatedAt(entity.getUpdatedAt().toString())
                .bookingId(entity.getBooking().getId())
                .build();
    }
}
