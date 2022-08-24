package com.passportoffice.controller;

import com.passportoffice.controller.mapper.PassportEntitiesMapper;
import com.passportoffice.controller.mapper.PersonEntitiesMapper;
import com.passportoffice.dto.PassportDto;
import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.dto.request.CreatePersonRequest;
import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.request.UpdatePersonRequest;
import com.passportoffice.dto.response.PassportResponse;
import com.passportoffice.dto.response.PersonResponse;
import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.Status;
import com.passportoffice.repository.PersonRepository;
import com.passportoffice.service.OfficeService;
import com.passportoffice.service.PersonService;
import com.passportoffice.validation.PassportStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class PersonsApiController {

  private final OfficeService officeService;
  private final PersonService personService;
  private final PassportEntitiesMapper passportEntitiesMapper;
  private final PersonEntitiesMapper personEntitiesMapper;
  private final PersonRepository personRepository;

  @PostMapping(path = "/persons/{id}/passports")
  @ResponseStatus(code = HttpStatus.CREATED)
  public PassportResponse addPassport(
      @PathVariable("id") String personId, @Valid @RequestBody CreatePassportRequest body)
      throws PassportNotFoundException {

    PassportDto passportDto =
        officeService.createPassport(
            personId,
            body.getType(),
            body.getNumber(),
            body.getGivenDate(),
            body.getDepartmentCode(),
            body.getStatus());

    return passportEntitiesMapper.toResponse(passportDto);
  }

  @PostMapping(path = "/persons")
  @ResponseStatus(code = HttpStatus.CREATED)
  public PersonResponse createPerson(@Valid @RequestBody CreatePersonRequest body) {

    log.info("Request to create person entity with body [{}]", body.toString());
    PersonResponse personResponse =
        personEntitiesMapper.toResponse(
            personService.createPerson(
                body.getFirstName(),
                body.getLastName(),
                body.getDateOfBirth(),
                body.getBirthCountry()));
    log.info("Created person [{}]", personResponse.toString());
    return personResponse;
  }

  @DeleteMapping(path = "/persons/{id}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public PersonResponse deletePerson(@PathVariable("id") String id) {
    return personEntitiesMapper.toResponse(personService.deletePerson(id));
  }

  @GetMapping(path = "/persons/{id}/passports")
  @ResponseStatus(code = HttpStatus.OK)
  public List<PassportResponse> getPassportsByFilter(
      @PathVariable("id") String id,
      @PassportStatus(
              anyOf = {Status.INACTIVE, Status.LOST, Status.ACTIVE},
              enumClass = Status.class)
          @RequestParam(value = "status", required = false)
          String status,
      @RequestParam(value = "startDate", required = false) String startDate,
      @RequestParam(value = "endDate", required = false) String endDate) {

    List<PassportResponse> passportResponses = new ArrayList<>();

    LocalDate localStartDate = startDate == null ? null : LocalDate.parse(startDate);
    LocalDate localEndDate = endDate == null ? null : LocalDate.parse(endDate);

    officeService
        .getPassportsByFilter(id, localStartDate, localEndDate, status)
        .forEach(
            passportDto -> passportResponses.add(passportEntitiesMapper.toResponse(passportDto)));
    return passportResponses;
  }

  @GetMapping(path = "/persons")
  @ResponseStatus(code = HttpStatus.OK)
  public List<PersonResponse> getPersonsByFilter(
      @RequestParam(value = "passportNumber", required = false) Long passportNumber) {

    List<PersonResponse> personResponses = new ArrayList<>();
    officeService
        .getPersonsByFilter(passportNumber)
        .forEach(personDto -> personResponses.add(personEntitiesMapper.toResponse(personDto)));
    return personResponses;
  }

  @GetMapping(path = "/persons/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public PersonResponse getPersonsById(@PathVariable("id") String id) {
    return personEntitiesMapper.toResponse(personService.getPerson(id));
  }

  @PutMapping(path = "/persons/{personId}/passports/{passportId}")
  @ResponseStatus(code = HttpStatus.ACCEPTED)
  public PassportResponse updatePassportPerPerson(
      @PathVariable("personId") String personId,
      @PathVariable("passportId") String passportId,
      @Valid @RequestBody UpdatePassportRequest body) {

    PassportDto updatedPassport;
    try {
      updatedPassport = officeService.updatePassportPerPerson(personId, passportId, body);
    } catch (PassportNotFoundException passportNotFoundException) {
      throw new NoSuchElementException("There is no passport with such id");
    }

    return passportEntitiesMapper.toResponse(updatedPassport);
  }

  @PutMapping(path = "/persons/{id}")
  @ResponseStatus(code = HttpStatus.ACCEPTED)
  public PersonResponse updatePerson(
      @PathVariable("id") String personId, @Valid @RequestBody UpdatePersonRequest body) {
    return personEntitiesMapper.toResponse(
        personService.updatePerson(
            personId,
            body.getFirstName(),
            body.getLastName(),
            body.getDateOfBirth(),
            body.getBirthCountry()));
  }
}
