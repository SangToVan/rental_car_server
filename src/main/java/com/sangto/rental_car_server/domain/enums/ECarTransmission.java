package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ECarTransmission {
    AUTOMATIC("Automatic"),
    MANUAL("Manual");
    private final String title;
}
