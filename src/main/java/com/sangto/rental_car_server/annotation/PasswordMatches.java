package com.sangto.rental_car_server.annotation;

import com.sangto.rental_car_server.validator.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PasswordMatches {
    String message() default "Password and confirm password must match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}