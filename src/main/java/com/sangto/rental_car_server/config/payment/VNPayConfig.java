package com.sangto.rental_car_server.config.payment;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class VNPayConfig {

    @Value("${payment.vnpay.tmnCode}")
    private String tmnCode;

    @Value("${payment.vnpay.secretKey}")
    private String secretKey;

    @Value("${payment.vnpay.returnUrl}")
    private String returnUrl;

    @Value("${payment.vnpay.vnpUrl}")
    private String vnpUrl;

    @Value("${payment.vnpay.version}")
    private String version;

    @Value("${payment.vnpay.command}")
    private String command;

    @Value("${payment.vnpay.orderType}")
    private String orderType;

    public Map<String, String> getConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.version);
        vnpParamsMap.put("vnp_Command", this.command);
        vnpParamsMap.put("vnp_TmnCode", this.tmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.returnUrl);
        return vnpParamsMap;
    }
}
