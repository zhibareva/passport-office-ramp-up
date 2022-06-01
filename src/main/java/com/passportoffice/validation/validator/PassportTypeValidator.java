package com.passportoffice.validation.validator;

import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.enums.PassportType;
import com.passportoffice.service.impl.OfficeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PassportTypeValidator {
    @Autowired
    OfficeServiceImpl officeService;

    public boolean isValid(Long personId, PassportType passportType) {
        Collection<PassportDto> passportDtos = officeService.getPassportPerPerson(personId.toString());
        return passportDtos.stream().noneMatch(passportDto -> passportDto.getType().equals(passportType));
    }
}
