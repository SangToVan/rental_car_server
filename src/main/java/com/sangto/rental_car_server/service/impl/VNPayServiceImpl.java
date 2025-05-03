package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.config.payment.VNPayConfig;
import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.EPaymentStatus;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.service.VNPayService;
import com.sangto.rental_car_server.utility.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayServiceImpl implements VNPayService {

    private final VNPayConfig vnPayConfig;

    @Value("${payment.vnPay.max_time}")
    private int maxPaymentTime; // in second

    private final String SUCCESS_CODE = "00";


    @Override
    public String createPaymentUrl(Payment payment, HttpServletRequest request) {
        if(payment.getPaymentMethod() != EPaymentMethod.BANK)
            throw new AppException("Payment type is not BANK_TRANSFER");
        if(payment.getPaymentStatus() == EPaymentStatus.FAILED)
            throw new AppException("Payment expired");
        if(payment.getPaymentStatus() != EPaymentStatus.PENDING)
            throw new AppException("Payment status is not PENDING");
        LocalDateTime expiredDate = payment.getExpiredAt();
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        LocalDateTime currentTime = LocalDateTime.now();
        if(currentTime.isBefore(expiredDate)){
            payment.setPaymentStatus(EPaymentStatus.FAILED);
//            order.setPayment(payment);
            throw new AppException("Payment expired");
        }
        Map<String, String> params = vnPayConfig.getConfig();
        long diffInMillis = expiredDate.getSecond() - currentTime.getSecond();
        int remainingSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(diffInMillis);
        int allowedTime = Math.min(remainingSeconds, maxPaymentTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(now.getTime());
        params.put("vnp_CreateDate", vnp_CreateDate);
        now.add(Calendar.SECOND, allowedTime);
        String vnp_ExpireDate = formatter.format(now.getTime());
        params.put("vnp_ExpireDate", vnp_ExpireDate);
        params.put("vnp_Amount", String.valueOf(payment.getAmount()));
        String ref = payment.getBookingId() + "-" + System.currentTimeMillis();
        params.put("vnp_TxnRef", ref);
        params.put("vnp_OrderInfo", "Thanh toan don hang: " + payment.getBookingId());
        String ipAddr = VNPayUtil.getIpAddress(request);
        params.put("vnp_IpAddr", ipAddr);
        String queryString = VNPayUtil.createPaymentUrl(params, true);
        String hashData = VNPayUtil.createPaymentUrl(params, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryString += "&vnp_SecureHash=" + vnpSecureHash;
        return vnPayConfig.getVnp_PayUrl() + "?" + queryString;
    }

    @Override
    public boolean checkPayment(Payment payment, Map<String, String> params) {
        String code = params.get("vnp_ResponseCode");
        if(code.equals(SUCCESS_CODE)){
//            long amount = Long.parseLong(params.get("vnp_Amount")) / 100L;
            BigDecimal amount = new BigDecimal(params.get("vnp_Amount"));
//            Payment payment = order.getPayment();
            return amount.equals(payment.getAmount());
        }
        else return false;
    }
}
