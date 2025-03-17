package com.sangto.rental_car_server.validator;

import com.sangto.rental_car_server.annotation.StrongPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    private final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{7,}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches(PASSWORD_PATTERN);
    }
}
