package com.passportoffice.service;

import com.passportoffice.dto.PassportDto;
import com.passportoffice.dto.PersonDto;
import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface OfficeService {

  PassportDto createPassport(
      String personId,
      PassportType type,
      Long number,
      LocalDate givenDate,
      String depCode,
      Status status)
      throws PassportNotFoundException;

  List<PassportDto> getPassportPerPerson(String personId) throws PassportNotFoundException;

  List<PersonDto> getPersonsByFilter(Long passportNumber);

  PassportDto updatePassportPerPerson(
      String personId, String passportId, UpdatePassportRequest body)
      throws PassportNotFoundException;

  PassportDto deactivatePassport(String passportId) throws PassportNotFoundException;

  Set<PassportDto> getPassportsByFilter(
      String personId, LocalDate startDate, LocalDate endDate, String status);
}
