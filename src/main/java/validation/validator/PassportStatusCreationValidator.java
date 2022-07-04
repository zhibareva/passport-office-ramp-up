package validation.validator;

import com.passportoffice.model.Status;
import validation.ElementOfSubset;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PassportStatusCreationValidator implements ConstraintValidator<ElementOfSubset, Status> {
    private Status[] subset;

    @Override
    public void initialize(ElementOfSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(Status value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}