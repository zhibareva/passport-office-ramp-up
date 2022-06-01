package com.passportoffice.controller;

import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.service.impl.PassportServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@RestController
public class PassportsApiController implements PassportsApi {

    private static final Logger log = LoggerFactory.getLogger(PassportsApiController.class);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    private final PassportServiceImpl passportService;

    @Autowired
    public PassportsApiController(PassportServiceImpl passportService) {
        this.passportService = passportService;
    }

    public PassportDto deletePassport(@Valid @PathVariable("id") Long id) {
        return passportService.deletePassportById(id);
    }

    public PassportDto getPassportById(@Valid @PathVariable("id") Long id) {
        return passportService.getPassportById(id);
    }

    public PassportDto updatePassport(@Valid @PathVariable("id") String id, @Valid @RequestBody UpdatePassportRequest body) {
        return passportService.updatePassport(
                Long.parseLong(id),
                body.getType(),
                body.getNumber(),
                body.getGivenDate(),
                body.getExpirationDate(),
                body.getDepartmentCode(),
                body.getStatus());
    }
}
