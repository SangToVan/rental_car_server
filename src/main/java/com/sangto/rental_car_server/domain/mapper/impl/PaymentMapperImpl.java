package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.payment.AddPaymentRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.domain.mapper.PaymentMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentMapperImpl implements PaymentMapper {
    @Override
    public Payment addPaymentRequestDTOtoEntity(AddPaymentRequestDTO requestDTO) {
        return Payment.builder()
                .amount(new BigDecimal(requestDTO.amount()))
                .paymentMethod(requestDTO.paymentMethod())
                .paymentStatus(requestDTO.paymentStatus())
                .bookingId(requestDTO.bookingId())
                .build();
    }

    @Override
    public PaymentResponseDTO toPaymentResponseDTO(Payment entity) {
        return PaymentResponseDTO.builder()
                .amount(entity.getAmount().toString())
                .paymentMethod(entity.getPaymentMethod())
                .paymentStatus(entity.getPaymentStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .bookingId(entity.getBookingId())
                .build();
    }
}
