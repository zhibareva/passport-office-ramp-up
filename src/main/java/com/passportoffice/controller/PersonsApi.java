package com.passportoffice.controller;

import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.dto.request.CreatePersonRequest;
import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.request.UpdatePersonRequest;
import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.dto.response.PersonDto;
import com.passportoffice.validation.PassportNumber;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Validated
public interface PersonsApi {

    @RequestMapping(value = "/persons/{id}/passports", produces = { "application/json" }, consumes = { "application/json" }, method = RequestMethod.POST)
    PassportDto addPassport(@PathVariable("id") String id, @Valid @RequestBody CreatePassportRequest body);

    @RequestMapping(value = "/persons", produces = { "application/json" }, consumes = { "*/*" }, method = RequestMethod.POST)
    PersonDto createPerson(@Valid @RequestBody CreatePersonRequest body);

    @RequestMapping(value = "/persons/{id}", produces = { "application/json" }, method = RequestMethod.DELETE)
    PersonDto deletePerson(@PathVariable("id") String id);

    @RequestMapping(value = "/persons/{id}/passports", produces = { "application/json" }, method = RequestMethod.GET)
    List<PassportDto> getPassportsByFilter(@PathVariable("id") String id, @Valid @RequestParam(value = "status", required = false) String status, @Valid @RequestParam(value = "startDate", required = false) LocalDate startDate, @Valid @RequestParam(value = "endDate", required = false) LocalDate endDate);

    @RequestMapping(value = "/persons", produces = { "application/json" }, method = RequestMethod.GET)
    List<PersonDto> getPersonsByFilter(@Valid @RequestParam(value = "passportNumber", required = false) Long passportNumber);

    @RequestMapping(value = "/persons/{id}", produces = { "application/json" }, method = RequestMethod.GET)
    PersonDto getPersonsById(@PathVariable("id") String id);

    @RequestMapping(value = "/persons/{personId}/passports/{passportId}", produces = { "application/json" }, consumes = { "*/*" }, method = RequestMethod.PUT)
    PassportDto updatePassportPerPerson(@PathVariable("personId") Long personId, @PathVariable("passportId") Long passportId, @Valid @RequestBody UpdatePassportRequest body);

    @RequestMapping(value = "/persons/{id}", produces = { "application/json" }, consumes = { "*/*" }, method = RequestMethod.PUT)
    PersonDto updatePerson(@PathVariable("id") String id, @Valid @RequestBody UpdatePersonRequest body);

}

