package com.passportoffice.controller;

import com.passportoffice.controller.mapper.PassportEntitiesMapper;
import com.passportoffice.dto.response.PassportResponse;
import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.service.OfficeService;
import com.passportoffice.service.PassportService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class PassportsApiController {

  private final PassportService passportService;
  private final OfficeService officeService;
  private final PassportEntitiesMapper mapper;

  @SneakyThrows
  @DeleteMapping(path = "/passports/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public PassportResponse deletePassport(@PathVariable("id") String id) {
    return mapper.toResponse(passportService.deletePassportById(id));
  }

  @SneakyThrows
  @GetMapping(path = "/passports/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public PassportResponse getPassportById(@PathVariable("id") String id) {
    return mapper.toResponse(passportService.getPassportById(id));
  }

  @PostMapping(path = "/passports/{id}/deactivate")
  @ResponseStatus(code = HttpStatus.OK)
  public PassportResponse deactivatePassport(@PathVariable("id") String id)
      throws PassportNotFoundException {
    return mapper.toResponse(officeService.deactivatePassport(id));
  }
}
