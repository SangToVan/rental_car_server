package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EBookingStatus {
    PENDING("Pending"),
    PAID("Paid"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),
    IN_PROGRESS("In Progress"),
    RETURNED("Returned"),
    COMPLETED("Completed");

    private final String title;
}
