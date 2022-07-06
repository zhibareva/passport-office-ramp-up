package validation.validator;

import com.passportoffice.dto.PassportDto;
import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import com.passportoffice.exception.InvalidPassportTypeException;
import com.passportoffice.service.impl.OfficeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PassportTypeValidator {
    @Autowired
    OfficeServiceImpl officeService;

    public void validatePassportType(Long personId, PassportType passportType) throws PassportNotFoundException {
        Collection<PassportDto> passportDtos = officeService.getPassportPerPerson(personId);
        if (passportDtos.stream().anyMatch(passportDto -> passportDto.getType().equals(passportType)) &&
                passportDtos.stream().anyMatch(passportDto -> passportDto.getStatus().equals(Status.ACTIVE))) {

            throw new InvalidPassportTypeException("User cannot has two passports with the same type");
        }
    }
}
