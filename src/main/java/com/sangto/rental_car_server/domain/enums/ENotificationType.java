package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ENotificationType {
    CAR("Trạng thái xe"),
    RENTAL("Trạng thái đơn thuê"),
    PAYMENT("Trạng thái thanh toán"),
    WALLET("Giao dịch ví");

    private final String title;
}
