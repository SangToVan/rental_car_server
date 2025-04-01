package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EscrowStatus {
    PENDING("Pending"),
    REFUNDED("Refunded"),
    RELEASED("Released");

    private final String title;
}
