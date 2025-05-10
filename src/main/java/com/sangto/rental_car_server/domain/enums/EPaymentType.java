package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EPaymentType {

    DEPOSIT("Deposit"),
    FULL("Full"),
    REMAINING("Remaining"),
    REFUND("Refund"),
    RELEASE("Release");

    private final String title;
}
