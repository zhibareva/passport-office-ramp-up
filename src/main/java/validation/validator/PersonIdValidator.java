package validation.validator;

import com.passportoffice.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import validation.PersonId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PersonIdValidator implements ConstraintValidator<PersonId, Long> {

    @Autowired
    PersonRepository personRepository;

    @Override
    public boolean isValid(Long number, ConstraintValidatorContext constraintValidatorContext) {
        return personRepository.getPersons().get(number) == null;
    }

}
