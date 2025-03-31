package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.payment.AddPaymentRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.mapper.PaymentMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.PaymentRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepo;
    private final PaymentRepository paymentRepo;
    private final PaymentMapper paymentMapper;

    @Override
    public Response<List<PaymentResponseDTO>> getListByUserId(Integer userId) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("This user is not exist");
        User user = findUser.get();

        List<Payment> list = paymentRepo.findAllByUserId(user.getId());
        List<PaymentResponseDTO> li = list.stream().map(paymentMapper :: toPaymentResponseDTO).toList();

        return Response.successfulResponse(
                "Get list payment successfully",
                li
        );
    }

    @Override
    public Payment addPayment(AddPaymentRequestDTO requestDTO) {
        Optional<User> findUser = userRepo.findById(requestDTO.userId());
        if (findUser.isEmpty()) throw new AppException("This user is not exist");
        User user = findUser.get();

        try {
            Payment newPayment = paymentMapper.addPaymentRequestDTOtoEntity(requestDTO);
            newPayment.setUser(user);
            paymentRepo.save(newPayment);
            return newPayment;
        } catch (Exception e) {
            throw new AppException("Add new payment failed" + e.getMessage());
        }
    }
}
