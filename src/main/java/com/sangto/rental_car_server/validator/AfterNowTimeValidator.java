package com.sangto.rental_car_server.validator;

import com.sangto.rental_car_server.annotation.AfterNowTime;
import com.sangto.rental_car_server.utility.TimeUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class AfterNowTimeValidator implements ConstraintValidator<AfterNowTime, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return TimeUtil.convertToDateTime(value).isAfter(LocalDateTime.now());
    }
}

