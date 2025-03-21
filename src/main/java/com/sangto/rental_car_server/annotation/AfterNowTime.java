package com.sangto.rental_car_server.annotation;

import com.sangto.rental_car_server.validator.AfterNowTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = AfterNowTimeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AfterNowTime {
    String message() default "The start rental time must be after now";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
