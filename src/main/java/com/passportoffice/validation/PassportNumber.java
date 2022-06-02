package com.passportoffice.validation;

import com.passportoffice.validation.validator.PassportNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassportNumberValidator.class)
public @interface PassportNumber {

    String message() default "invalid passport number supplied";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
