package com.sangto.rental_car_server.annotation;

import com.sangto.rental_car_server.validator.StrongPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrongPassword {
    String message() default "Password must contain at least one number, one numeral, and seven characters.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
