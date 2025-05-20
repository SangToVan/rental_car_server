package com.sangto.rental_car_server.utility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RentalCalculateUtil {

    private static final BigDecimal INSURANCE_FEE_PER_DAY = new BigDecimal("100000");     // ví dụ: 50.000 VND/ngày
    private static final BigDecimal PASSENGER_INSURANCE_FEE_PER_DAY = new BigDecimal("50000");
    private static final Double SYSTEM_FEE = 0.1;

    public static Long calculateHour(LocalDateTime startDate, LocalDateTime endDate) {
        return ChronoUnit.HOURS.between(startDate, endDate);
    }

    public static Long calculateRentalDays(LocalDateTime startDate, LocalDateTime endDate) {
        return ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()) + 1;
    }

    public static BigDecimal calculateTotalFee(LocalDateTime startDate, LocalDateTime endDate, BigDecimal basicPrice) {
        Long diff = calculateRentalDays(startDate, endDate);
        BigDecimal dailyTotal = basicPrice.add(INSURANCE_FEE_PER_DAY).add(PASSENGER_INSURANCE_FEE_PER_DAY);
        return dailyTotal.multiply(BigDecimal.valueOf(diff));
    }

    public static BigDecimal calculateInsuranceFee(LocalDateTime startDate, LocalDateTime endDate) {
        Long diff = calculateRentalDays(startDate, endDate);
        BigDecimal insuranceFee = new BigDecimal(BigInteger.ZERO);
        insuranceFee = insuranceFee.add(INSURANCE_FEE_PER_DAY).add(PASSENGER_INSURANCE_FEE_PER_DAY);
        return insuranceFee.multiply(BigDecimal.valueOf(diff));
    }

    public static BigDecimal calculateSystemFee(BigDecimal totalFee, BigDecimal insuranceFee) {
        return (totalFee.subtract(insuranceFee)).multiply(BigDecimal.valueOf(SYSTEM_FEE));
    }
}
