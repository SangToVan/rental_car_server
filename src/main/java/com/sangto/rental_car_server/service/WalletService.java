package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.wallet.UpdWalletDTO;
import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.responses.Response;

import java.math.BigDecimal;

public interface WalletService {

    Response<WalletResponseDTO> getWalletDetail(Integer userId);

    Response<WalletResponseDTO> updateWallet(Integer userId, UpdWalletDTO updWalletDTO);

    void creditWallet(Integer walletId, BigDecimal amount);

    void debitWallet(Integer walletId, BigDecimal amount);

    Response<String> transferWallet(Integer fromWalletId, Integer toWalletId, BigDecimal amount);

    Response<String> paymentBooking(Integer customerId, BigDecimal amount, Integer bookingId);

    Response<String> releaseBooking(Integer ownerId, BigDecimal amount, Integer bookingId);

    Response<String> refundBooking(Integer customerId, BigDecimal amount, Integer bookingId);
}
