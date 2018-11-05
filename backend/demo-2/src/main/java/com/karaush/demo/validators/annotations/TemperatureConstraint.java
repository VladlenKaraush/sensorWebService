package com.karaush.demo.validators.annotations;

import com.karaush.demo.validators.Impl.TemperatureValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TemperatureValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TemperatureConstraint {

    String message() default "Invalid temperature range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
