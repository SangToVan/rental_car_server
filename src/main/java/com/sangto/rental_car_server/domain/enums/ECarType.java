package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ECarType {
    SEDAN("Sedan"),
    SUV("Suv"),
    HATCHBACK("Hatchback"),
    MPV("Mpv"),
    PICKUP("Pickup"),
    ELECTRIC("Electric"),
    HYBRID("Hybrid"),
    SPORTS("Sports");
    private final String title;
}
