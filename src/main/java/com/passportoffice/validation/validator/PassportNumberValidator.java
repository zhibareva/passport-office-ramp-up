package com.passportoffice.validation.validator;

import com.passportoffice.model.Passport;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.validation.PassportNumber;
import java.util.Collection;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PassportNumberValidator implements ConstraintValidator<PassportNumber, Long> {

  @Autowired PassportRepository passportRepository;

  @Override
  public boolean isValid(Long number, ConstraintValidatorContext constraintValidatorContext) {
    Collection<Passport> passportDtos = passportRepository.getPassports().values();
    return passportDtos.stream().noneMatch(passportDto -> passportDto.getNumber().equals(number));
  }
}
