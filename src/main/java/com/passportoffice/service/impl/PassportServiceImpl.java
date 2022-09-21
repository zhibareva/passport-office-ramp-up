package com.passportoffice.service.impl;

import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.Passport;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.service.PassportService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PassportServiceImpl implements PassportService {

  private final PassportRepository passportRepository;

  @SneakyThrows
  @Override
  public Passport getPassportById(String passportId) {
    log.info("Searching for passport with passportId [{}]", passportId);
    return passportRepository
        .findById(passportId)
        .orElseThrow(() -> new PassportNotFoundException("There is no passport with passportId"));
  }

  @SneakyThrows
  @Override
  public Passport deletePassportById(String passportId) {
    log.info("Deleting passport with passportId [{}]", passportId);
    return passportRepository
        .deleteById(passportId)
        .orElseThrow(() -> new PassportNotFoundException("There is no passport with passportId"));
  }
}
