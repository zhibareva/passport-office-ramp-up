package validation.validator;

import com.passportoffice.dto.PassportDto;
import com.passportoffice.repository.PassportRepository;
import org.springframework.stereotype.Component;
import validation.PassportNumber;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

@Component
public class PassportNumberValidator implements ConstraintValidator<PassportNumber, Long> {

    @Autowired
    PassportRepository passportRepository;

    @Override
    public boolean isValid(Long number, ConstraintValidatorContext constraintValidatorContext) {
        Collection<PassportDto> passportDtos = passportRepository.getPassports().values();
        return passportDtos.stream().noneMatch(passportDto -> passportDto.getNumber().equals(number));
    }

}
