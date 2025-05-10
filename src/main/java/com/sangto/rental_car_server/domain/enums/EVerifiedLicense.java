package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EVerifiedLicense {
    VERIFIED("Đã xác thực"),
    UNVERIFIED("Xác thực thất bại"),
    WAITING("Chờ xác thực");

    private final String title;
}
