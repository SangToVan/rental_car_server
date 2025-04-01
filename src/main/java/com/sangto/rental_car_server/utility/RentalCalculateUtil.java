package com.sangto.rental_car_server.utility;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RentalCalculateUtil {

    public static Long calculateHour(LocalDateTime startDate, LocalDateTime endDate) {
        return ChronoUnit.HOURS.between(startDate, endDate);
    }

    public static BigDecimal calculateRentalFee(LocalDateTime startDate, LocalDateTime endDate, BigDecimal basicPrice) {
        Long diff = calculateHour(startDate, endDate);
        return basicPrice.multiply(new BigDecimal(diff / 24));
    }
}
