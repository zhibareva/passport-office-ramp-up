package com.passportoffice.controller;

import com.passportoffice.dto.response.PassportResponse;
import com.passportoffice.service.OfficeService;
import com.passportoffice.utils.mapper.PassportEntitiesMapper;
import com.passportoffice.service.impl.PassportServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Slf4j
public class PassportsApiController {

    private final PassportServiceImpl passportService;
    private final OfficeService officeService;
    private final PassportEntitiesMapper mapper;

    @RequestMapping(value = "/passports/{id}", produces = {"application/json"}, method = RequestMethod.DELETE)
    public PassportResponse deletePassport(@Valid @PathVariable("id") Long id) {
        return mapper.toResponse(passportService.deletePassportById(id));
    }

    @RequestMapping(value = "/passports/{id}", produces = {"application/json"}, method = RequestMethod.GET)
    public PassportResponse getPassportById(@Valid @PathVariable("id") Long id) {
        return mapper.toResponse(passportService.getPassportById(id));
    }

    @RequestMapping(value = "/passports/{id}/deactivate", produces = {"application/json"}, method = RequestMethod.PUT)
    public PassportResponse deactivatePassport(@Valid @PathVariable("id") Long id) {
        return mapper.toResponse(officeService.deactivatePassport(id));
    }
}
