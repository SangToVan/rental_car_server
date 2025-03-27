package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EWalletType {
    SERVICE_FEE("Service fee"),
    RENTAL_FEE("Rental fee"),
    RESERVE("Reserve");

    private final String title;
}
