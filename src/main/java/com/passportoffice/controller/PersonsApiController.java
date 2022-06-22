package com.passportoffice.controller;

import com.passportoffice.dto.PassportDto;
import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.dto.request.CreatePersonRequest;
import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.request.UpdatePersonRequest;
import com.passportoffice.dto.response.PassportResponse;
import com.passportoffice.dto.response.PersonResponse;
import com.passportoffice.mapper.PassportEntitiesMapper;
import com.passportoffice.mapper.PersonEntitiesMapper;
import com.passportoffice.model.Status;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.repository.PersonRepository;
import com.passportoffice.service.OfficeService;
import com.passportoffice.service.PassportService;
import com.passportoffice.service.PersonService;
import com.passportoffice.utils.DataGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import validation.validator.PassportTypeValidator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class PersonsApiController {

    private final PassportService passportService;
    private final OfficeService officeService;
    private final PersonService personService;
    private final PassportTypeValidator validator;
    private final PassportEntitiesMapper passportEntitiesMapper;
    private final PersonEntitiesMapper personEntitiesMapper;
    private final PersonRepository personRepository;
    private final PassportRepository passportRepository;

    @RequestMapping(value = "/persons/{id}/passports", produces = { "application/json" }, consumes = { "application/json" }, method = RequestMethod.POST)
    public PassportResponse addPassport(@PathVariable("id") Long personId,
                                        @Valid @RequestBody CreatePassportRequest body) {

        validator.validatePassportType(personId, body.getType());
        Long passportId = passportRepository.generateId();

        PassportDto passportDto = officeService.createPassport(
                passportId,
                personId,
                body.getType(),
                body.getNumber(),
                body.getGivenDate(),
                body.getDepartmentCode(),
                body.getStatus()
        );

        return passportEntitiesMapper.toResponse(passportDto);
    }

    @RequestMapping(value = "/persons", produces = { "application/json" }, consumes = { "*/*" }, method = RequestMethod.POST)
    public PersonResponse createPerson(@Valid @RequestBody CreatePersonRequest body) {
        Long personId = personRepository.generateId();
        log.info("Request to create person entity with body [{}]", body);
        return personEntitiesMapper.toResponse(
                personService.createPerson(
                        personId,
                        body.getFirstName(),
                        body.getLastName(),
                        body.getDateOfBirth(),
                        body.getBirthCountry()
                ));

    }
    @RequestMapping(value = "/persons/{id}", produces = { "application/json" }, method = RequestMethod.DELETE)
    public PersonResponse deletePerson(@PathVariable("id") Long id) {
        return personEntitiesMapper.toResponse(personService.deletePerson(id));
    }

    @RequestMapping(value = "/persons/{id}/passports", produces = { "application/json" }, method = RequestMethod.GET)
    public List<PassportResponse> getPassportsByFilter(@PathVariable("id") Long id,
                                                       @Valid @RequestParam(value = "status", required = false) String status,
                                                       @Valid @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                                       @Valid @RequestParam(value = "endDate", required = false) LocalDate endDate) {

        List<PassportDto> filteredPassports = officeService.getPassportPerPerson(id);
        log.info("Passports of person [{}]", filteredPassports);
        List<PassportResponse> passportResponses = new ArrayList<>();

        officeService.getPassportsByFilter(
                filteredPassports,
                startDate,
                endDate,
                status
        ).forEach(passportDto -> passportResponses.add(passportEntitiesMapper.toResponse(passportDto)));
        log.info("Passports after filter [{}]", passportResponses);
        return passportResponses;

    }

    @RequestMapping(value = "/persons", produces = { "application/json" }, method = RequestMethod.GET)
    public List<PersonResponse> getPersonsByFilter(
            @Valid @RequestParam(value = "passportNumber", required = false) Long passportNumber) {

        List<PersonResponse> personResponses = new ArrayList<>();
        officeService.getPersonsByFilter(passportNumber).forEach(
                personDto -> personResponses.add(personEntitiesMapper.toResponse(personDto)));
        return personResponses;
    }

    @RequestMapping(value = "/persons/{id}", produces = { "application/json" }, method = RequestMethod.GET)
    public PersonResponse getPersonsById(@PathVariable("id") Long id) {
        return personEntitiesMapper.toResponse(personService.getPerson(id));
    }

    @RequestMapping(value = "/persons/{personId}/passports/{passportId}", produces = { "application/json" }, consumes = { "*/*" }, method = RequestMethod.PUT)
    public PassportResponse updatePassportPerPerson(@PathVariable("personId") Long personId,
                                                    @PathVariable("passportId") Long passportId,
                                                    @Valid @RequestBody UpdatePassportRequest body) {

        officeService.updatePassportPerPerson(personId, passportId, body);

        PassportDto updatedPassport = passportService.getPassportById(passportId);

        if (updatedPassport.getStatus().equals(Status.LOST))
            return addPassport(
                    personId,
                    new CreatePassportRequest(
                            updatedPassport.getType(),
                            DataGenerator.generatePassportNumber(),
                            DataGenerator.getCurrentDate().plusDays(3),
                            updatedPassport.getDepartmentCode(),
                            Status.ACTIVE
                    )
            );
        else
            return passportEntitiesMapper.toResponse(updatedPassport);
    }

    @RequestMapping(value = "/persons/{id}", produces = { "application/json" }, consumes = { "*/*" }, method = RequestMethod.PUT)
    public PersonResponse updatePerson(@PathVariable("id") Long personId,
                                       @Valid @RequestBody UpdatePersonRequest body) {
        return personEntitiesMapper.toResponse(
                personService.updatePerson(
                        personId,
                        body.getFirstName(),
                        body.getLastName(),
                        body.getDateOfBirth(),
                        body.getBirthCountry()
                ));
    }

}
