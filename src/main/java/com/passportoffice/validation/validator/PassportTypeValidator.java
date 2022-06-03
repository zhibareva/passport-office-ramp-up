package com.passportoffice.validation.validator;

import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.enums.PassportType;
import com.passportoffice.enums.Status;
import com.passportoffice.exception.InvalidPassportTypeException;
import com.passportoffice.service.OfficeService;
import com.passportoffice.service.impl.OfficeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PassportTypeValidator {
    @Autowired
    OfficeService officeService;

    public void validatePassportType(Long personId, PassportType passportType) {
        Collection<PassportDto> passportDtos = officeService.getPassportPerPerson(personId.toString());
        if (passportDtos.stream().anyMatch(passportDto -> passportDto.getType().equals(passportType)) &&
                passportDtos.stream().anyMatch(passportDto -> passportDto.getStatus().equals(Status.ACTIVE))) {

            throw new InvalidPassportTypeException("User cannot has two passports with the same type");
        }
    }
}
