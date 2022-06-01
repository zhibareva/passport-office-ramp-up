package com.passportoffice.controller;

import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.response.PassportDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Validated
public interface PassportsApi {

    @RequestMapping(value = "/passports/{id}", produces = {"application/json"}, method = RequestMethod.DELETE)
    PassportDto deletePassport(@Valid @PathVariable("id") Long id);

    @RequestMapping(value = "/passports/{id}", produces = {"application/json"}, method = RequestMethod.GET)
    PassportDto getPassportById(@Valid @PathVariable("id") Long id);

    @RequestMapping(value = "/passports/{id}", produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.PUT)
    PassportDto updatePassport(@Valid @PathVariable("id") String id, @Valid @RequestBody UpdatePassportRequest body);

}


