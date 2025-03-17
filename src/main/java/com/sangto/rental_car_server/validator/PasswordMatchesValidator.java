package com.sangto.rental_car_server.validator;

import com.sangto.rental_car_server.annotation.PasswordMatches;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, AddUserRequestDTO> {

    @Override
    public boolean isValid(AddUserRequestDTO value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.password() != null && value.password().equals(value.confirmPassword());
    }
}
