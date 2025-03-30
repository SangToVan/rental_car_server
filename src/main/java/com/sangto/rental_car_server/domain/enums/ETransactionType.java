package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ETransactionType {
    DEPOSIT("Deposit"),
    TOP_UP("Top up"),
    WITHDRAW("Withdraw"),
    PAYMENT("Payment"),
    TRANSFER("Transfer"),
    REFUND("Refund");

    private final String title;
}
