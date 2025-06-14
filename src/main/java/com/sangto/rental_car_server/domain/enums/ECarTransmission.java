package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ECarTransmission {
    AUTOMATIC("Automatic"),
    MANUAL("Manual"),
    SEMI_AUTOMATIC("Semi-Automatic");

    private final String title;
}
