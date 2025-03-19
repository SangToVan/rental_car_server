package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ECarStatus {
    UNVERIFIED("Chưa xác thực"),  // Xe chưa được xác thực
    VERIFIED("Đã xác thực"),    // Xe đã được xác thực
    ACTIVE("Đang hoạt động"),      // Xe đang hoạt động, có thể được thuê
    SUSPENDED("Dừng cho thuê"),   // Xe bị dừng cho thuê
    RENTED("Đang được thuê");       // Xe đang được thuê

    private final String title;
}
