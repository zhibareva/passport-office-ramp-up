package com.passportoffice.validation.validator;

import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.validation.PassportNumber;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class PassportNumberValidator implements ConstraintValidator<PassportNumber, Long> {

    @Autowired
    PassportRepository passportRepository;

    @Override
    public boolean isValid(Long number, ConstraintValidatorContext constraintValidatorContext) {
        Collection<PassportDto> passportDtos = passportRepository.getPassports().values();
        return passportDtos.stream().noneMatch(passportDto -> passportDto.getNumber().equals(number));
    }

}
