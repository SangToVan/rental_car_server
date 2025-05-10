package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EUserRole {
    ADMIN("Administrator"),
    SYSTEM("System"),
    CUSTOMER("Customer"),
    OWNER("Car owner");

    private final String title;
}
