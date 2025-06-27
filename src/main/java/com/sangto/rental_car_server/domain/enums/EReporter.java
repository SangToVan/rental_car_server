package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EReporter {
    CUSTOMER("Customer"),
    OWNER("Car owner");

    private final String title;
}
