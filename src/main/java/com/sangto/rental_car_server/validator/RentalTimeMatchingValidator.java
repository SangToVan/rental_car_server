package com.sangto.rental_car_server.validator;

import com.sangto.rental_car_server.annotation.RentalTimeMatching;
import com.sangto.rental_car_server.utility.TimeUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class RentalTimeMatchingValidator implements ConstraintValidator<RentalTimeMatching, Object> {
    String startTime;
    String endTime;

    @Override
    public void initialize(RentalTimeMatching constraintAnnotation) {
        this.startTime = constraintAnnotation.startTime();
        this.endTime = constraintAnnotation.endTime();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String startTimeValue = (String) new BeanWrapperImpl(value).getPropertyValue(startTime);
        String endTimeValue = (String) new BeanWrapperImpl(value).getPropertyValue(endTime);
        return TimeUtil.convertToDateTime(endTimeValue).isAfter(TimeUtil.convertToDateTime(startTimeValue));
    }
}
