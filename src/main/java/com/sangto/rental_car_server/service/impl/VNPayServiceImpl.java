package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.config.payment.VNPayConfig;
import com.sangto.rental_car_server.domain.entity.Payment;
import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.EPaymentStatus;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.service.VNPayService;
import com.sangto.rental_car_server.utility.HmacUtil;
import com.sangto.rental_car_server.utility.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayServiceImpl implements VNPayService {

    private final VNPayConfig vnpayConfig;

    private static final int maxPaymentTime = 900; // 900 giây = 15 phút

    public String createPaymentUrl(Payment payment, HttpServletRequest request) {
        if (payment.getPaymentMethod() != EPaymentMethod.BANK)
            throw new AppException("Payment method is not BANK");

        if (payment.getPaymentStatus() == EPaymentStatus.EXPIRED)
            throw new AppException("Payment expired");

        if (payment.getPaymentStatus() != EPaymentStatus.PENDING)
            throw new AppException("Payment status is not PENDING");

        LocalDateTime expiredAt = payment.getExpiredAt();
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expiredAt)) {
            payment.setPaymentStatus(EPaymentStatus.EXPIRED);
            throw new AppException("Payment expired");
        }

        Map<String, String> params = vnpayConfig.getConfig();

        long diffInSeconds = java.time.Duration.between(now, expiredAt).getSeconds();
        int allowedTime = (int) Math.min(diffInSeconds, maxPaymentTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = now.format(formatter);
        String vnp_ExpireDate = now.plusSeconds(allowedTime).format(formatter);

        params.put("vnp_CreateDate", vnp_CreateDate);
        params.put("vnp_ExpireDate", vnp_ExpireDate);
        params.put("vnp_Amount", payment.getAmount().multiply(BigDecimal.valueOf(100)).setScale(0).toString());
        params.put("vnp_TxnRef", payment.getTransactionCode());
        params.put("vnp_OrderInfo", "Thanh toan don dat xe ma " + payment.getBooking().getId());

        String ipAddr = VNPayUtil.getIpAddress(request);
        params.put("vnp_IpAddr", ipAddr);

        String queryString = VNPayUtil.createPaymentUrl(params, true);
        String hashData = VNPayUtil.createPaymentUrl(params, false);
        String vnp_SecureHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);

        queryString += "&vnp_SecureHash=" + vnp_SecureHash;

        return vnpayConfig.getVnpUrl() + "?" + queryString;
    }



    @Override
    public boolean validateSignature(Map<String, String> vnpParams, String receivedHash) {
        Map<String, String> filteredParams = vnpParams.entrySet().stream()
                .filter(e -> !e.getKey().equals("vnp_SecureHash") && !e.getKey().equals("vnp_SecureHashType"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, TreeMap::new));

        String hashData = filteredParams.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        String calculatedHash = HmacUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
        boolean match = calculatedHash.equalsIgnoreCase(receivedHash);

        // ✅ FULL DEBUG
        log.info("======== [VNPay - XÁC THỰC CHỮ KÝ] ========");
        log.info("Received Hash     : {}", receivedHash);
        log.info("Hash Input        : {}", hashData);
        log.info("Calculated Hash   : {}", calculatedHash);
        log.info("Kết quả so sánh   : {}", match ? "✅ Hợp lệ" : "❌ Không hợp lệ");
        log.info("===========================================");

        return match;
    }


}
