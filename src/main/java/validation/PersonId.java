package validation;


import validation.validator.PassportIdValidator;
import validation.validator.PersonIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PersonIdValidator.class)
public @interface PersonId {

    String message() default "invalid passport number supplied";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
