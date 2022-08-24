package com.passportoffice.validation;

import com.passportoffice.validation.validator.PassportNumberValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassportNumberValidator.class)
public @interface PassportNumber {
  String message() default "invalid passport number supplied";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
