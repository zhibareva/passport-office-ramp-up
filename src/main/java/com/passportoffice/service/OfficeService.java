package com.passportoffice.service;

import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.Passport;
import com.passportoffice.model.Status;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Person;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface OfficeService {

  Passport createPassport(
      String personId,
      PassportType type,
      Long number,
      LocalDate givenDate,
      String depCode,
      Status status)
      throws PassportNotFoundException;

  List<Person> getPersonsByFilter(Long passportNumber);

  Passport updatePassportPerPerson(
      String personId, String passportId, PassportType type, Long number, LocalDate givenDate, LocalDate expirationDate, String departmentCode, Status status)
      throws PassportNotFoundException;

  Passport deactivatePassport(String passportId)
      throws PassportNotFoundException;

  Set<Passport> getPassportsByFilter(
      String personId, LocalDate startDate, LocalDate endDate, String status);
}
