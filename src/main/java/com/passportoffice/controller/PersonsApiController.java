package com.passportoffice.controller;

import com.passportoffice.controller.mapper.PassportEntitiesMapper;
import com.passportoffice.controller.mapper.PersonEntitiesMapper;
import com.passportoffice.controller.validation.PassportStatus;
import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.dto.request.CreatePersonRequest;
import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.request.UpdatePersonRequest;
import com.passportoffice.dto.response.PassportResponse;
import com.passportoffice.dto.response.PersonResponse;
import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.Passport;
import com.passportoffice.model.Status;
import com.passportoffice.service.OfficeService;
import com.passportoffice.service.PersonService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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

  @PostMapping(path = "/persons/{id}/passports")
  @ResponseStatus(code = HttpStatus.OK)
  public PassportResponse addPassport(
      @PathVariable("id") String personId, @Valid @RequestBody CreatePassportRequest body)
      throws PassportNotFoundException {

    Passport passportDto =
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
  @ResponseStatus(code = HttpStatus.OK)
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
      @PassportStatus(acceptedValues = {Status.INACTIVE, Status.LOST, Status.ACTIVE})
          @RequestParam(value = "status", required = false)
          String status,
      @DateTimeFormat(iso = ISO.DATE) @RequestParam(value = "startDate", required = false)
          LocalDate startDate,
      @DateTimeFormat(iso = ISO.DATE) @RequestParam(value = "endDate", required = false)
          LocalDate endDate) {

    return officeService.getPassportsByFilter(id, startDate, endDate, status).stream()
        .map(passportEntitiesMapper::toResponse)
        .collect(Collectors.toList());
  }

  @GetMapping(path = "/persons")
  @ResponseStatus(code = HttpStatus.OK)
  public List<PersonResponse> getPersonsByFilter(
      @RequestParam(value = "passportNumber", required = false) Long passportNumber) {
    return officeService.getPersonsByFilter(passportNumber).stream()
        .map(personEntitiesMapper::toResponse)
        .collect(Collectors.toList());
  }

  @GetMapping(path = "/persons/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public PersonResponse getPersonsById(@PathVariable("id") String id) {
    return personEntitiesMapper.toResponse(personService.getPerson(id));
  }

  @PutMapping(path = "/persons/{personId}/passports/{passportId}")
  @ResponseStatus(code = HttpStatus.OK)
  public PassportResponse updatePassportPerPerson(
      @PathVariable("personId") String personId,
      @PathVariable("passportId") String passportId,
      @Valid @RequestBody UpdatePassportRequest body) {

    Passport updatedPassport;
      updatedPassport =
          officeService.updatePassportPerPerson(
              personId,
              passportId,
              body.getType(),
              body.getNumber(),
              body.getGivenDate(),
              body.getExpirationDate(),
              body.getDepartmentCode(),
              body.getStatus());

    return passportEntitiesMapper.toResponse(updatedPassport);
  }

  @PutMapping(path = "/persons/{id}")
  @ResponseStatus(code = HttpStatus.OK)
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
