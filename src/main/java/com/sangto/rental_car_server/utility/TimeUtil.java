package com.sangto.rental_car_server.utility;

import com.sangto.rental_car_server.constant.TimeFormatConstant;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(TimeFormatConstant.DATETIME_FORMAT);

    public static LocalDateTime convertToDateTime(String timeStr) {
        return LocalDateTime.parse(timeStr, FORMATTER);
    }

    public static String formatToString(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    public static int getHoursDifference(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        Double hours = duration.toMinutes() / 60.0;
        return (int) Math.round(hours);
    }
}

