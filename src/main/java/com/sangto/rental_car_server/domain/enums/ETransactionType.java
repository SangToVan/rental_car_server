package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ETransactionType {
    DEPOSIT("Deposit"),
    TOP_UP("Top up"),
    WITHDRAW("Withdraw"),
    PAYMENT_BOOKING("Payment booking"),
    RELEASE_PAYMENT("Release payment"),
    REFUND_PAYMENT("Refund payment"),
    DEBIT("Debit"),
    CREDIT("Credit");

    private final String title;
}
