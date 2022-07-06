package validation.validator;

import com.passportoffice.repository.PassportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import validation.PassportId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PassportIdValidator implements ConstraintValidator<PassportId, Long> {

    @Autowired
    PassportRepository passportRepository;

    @Override
    public boolean isValid(Long number, ConstraintValidatorContext constraintValidatorContext) {
        return passportRepository.getPassports().get(number) == null;
    }

}
