package com.passportoffice.controller;

import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.dto.request.CreatePersonRequest;
import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.request.UpdatePersonRequest;
import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.dto.response.PersonDto;
import com.passportoffice.enums.Status;
import com.passportoffice.service.OfficeService;
import com.passportoffice.service.PassportService;
import com.passportoffice.service.PersonService;
import com.passportoffice.utils.DataGenerator;
import com.passportoffice.validation.validator.PassportTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
public class PersonsApiController implements PersonsApi {

    private static final Logger log = LoggerFactory.getLogger(PersonsApiController.class);

    private final PassportService passportService;
    private final OfficeService officeService;
    private final PersonService personService;
    private final PassportTypeValidator validator;

    @Autowired
    public PersonsApiController(PassportService passportService,
                                OfficeService officeService,
                                PersonService personService,
                                PassportTypeValidator validator) {
        this.passportService = passportService;
        this.officeService = officeService;
        this.personService = personService;
        this.validator = validator;
    }

    public PassportDto addPassport(@PathVariable("id") String id, @Valid @RequestBody CreatePassportRequest body) {

        validator.validatePassportType(Long.parseLong(id), body.getType());
        PassportDto passportDto = passportService.createPassport(
                    body.getType(),
                    body.getNumber(),
                    body.getGivenDate(),
                    body.getDepartmentCode(),
                    body.getStatus()
            );

            officeService.addPassportToPerson(Long.parseLong(id), passportDto);

        return passportDto;
    }

    public PersonDto createPerson(@Valid @RequestBody CreatePersonRequest body) {

        return personService.createPerson(
                body.getFirstName(),
                body.getLastName(),
                body.getDateOfBirth(),
                body.getBirthCountry());

    }

    public PersonDto deletePerson(@PathVariable("id") String id) {
        return personService.deletePerson(Long.parseLong(id));
    }

    public List<PassportDto> getPassportsByFilter(@PathVariable("id") String id, @Valid @RequestParam(value = "status", required = false) String status, @Valid @RequestParam(value = "startDate", required = false) LocalDate startDate, @Valid @RequestParam(value = "endDate", required = false) LocalDate endDate) {

        List<PassportDto> filteredPassports = officeService.getPassportPerPerson(id);
        return officeService.getPassportsByFilter(filteredPassports, startDate, endDate, status);

    }

    public List<PersonDto> getPersonsByFilter(@Valid @RequestParam(value = "passportNumber", required = false) Long passportNumber) {
        return officeService.getPersonsByFilter(passportNumber);
    }

    public PersonDto getPersonsById(@PathVariable("id") String id) {
        return personService.getPerson(Long.parseLong(id));
    }

    public PassportDto updatePassportPerPerson(@PathVariable("personId") Long personId, @PathVariable("passportId") Long passportId, @Valid @RequestBody UpdatePassportRequest body) {
        officeService.updatePassportPerPerson(personId, passportId, body);
        PassportDto updatedPassport = passportService.getPassportById(passportId);
        if (updatedPassport.getStatus().equals(Status.LOST))
            return addPassport(personId.toString(), new CreatePassportRequest(
                    updatedPassport.getType(),
                    DataGenerator.generatePassportNumber(),
                    DataGenerator.getCurrentDate().plusDays(3),
                    updatedPassport.getDepartmentCode(),
                    Status.ACTIVE
            ));
        else
            return updatedPassport;
    }

    public PersonDto updatePerson(@PathVariable("id") String id, @Valid @RequestBody UpdatePersonRequest body) {
        return personService.updatePerson(Long.parseLong(id), body.getFirstName(), body.getLastName(), body.getDateOfBirth(), body.getBirthCountry());
    }

}
