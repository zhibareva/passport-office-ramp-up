package validation;


import validation.validator.PassportIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassportIdValidator.class)
public @interface PassportId {

    String message() default "invalid passport number supplied";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
