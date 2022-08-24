package com.passportoffice.service.impl;

import com.passportoffice.controller.mapper.PassportEntitiesMapper;
import com.passportoffice.dto.PassportDto;
import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.Passport;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.service.PassportService;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PassportServiceImpl implements PassportService {

  private final PassportRepository passportRepository;
  private final PassportEntitiesMapper passportEntitiesMapper;

  @SneakyThrows
  @Override
  public PassportDto getPassportById(String passportId) {
    log.info("Searching for passport with passportId [{}]", passportId);
    Passport passport =
        passportRepository
            .findById(passportId)
            .orElseThrow(
                () -> new PassportNotFoundException("There is no passport with passportId"));
    return passportEntitiesMapper.toDto(passport);
  }

  @SneakyThrows
  @Override
  public PassportDto deletePassportById(String passportId) {
    log.info("Deleting passport with passportId [{}]", passportId);
    Passport passport =
        passportRepository
            .deleteById(passportId)
            .orElseThrow(
                () -> new PassportNotFoundException("There is no passport with passportId"));
    return passportEntitiesMapper.toDto(passport);
  }

  @Override
  public PassportDto updatePassport(
      String personId,
      String passportId,
      PassportType type,
      Long number,
      LocalDate givenDate,
      LocalDate expDate,
      String depCode,
      Status status) {
    log.info("Updating passport with fields [{}]", passportId);
    Passport passport =
        passportRepository.save(
            new Passport(passportId, personId, type, number, givenDate, expDate, depCode, status));
    return passportEntitiesMapper.toDto(passport);
  }
}
