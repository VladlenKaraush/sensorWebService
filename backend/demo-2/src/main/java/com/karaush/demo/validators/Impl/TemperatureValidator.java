package com.karaush.demo.validators.Impl;

import com.karaush.demo.validators.annotations.TemperatureConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TemperatureValidator implements ConstraintValidator<TemperatureConstraint, Double> {

    private static final int tempMax = 100;
    private static final int tempMin = -100;

    @Override
    public void initialize(TemperatureConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(Double d, ConstraintValidatorContext constraintValidatorContext) {
        return d >= tempMin && d <= tempMax;
    }
}
