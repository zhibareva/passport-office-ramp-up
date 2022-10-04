package com.passportoffice.service.impl;

import com.passportoffice.exception.InvalidPassportException;
import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.Passport;
import com.passportoffice.model.Status;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Person;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.repository.PersonRepository;
import com.passportoffice.service.OfficeService;
import com.passportoffice.utils.IdGenerator;
import com.passportoffice.utils.PassportNumberGenerator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OfficeServiceImpl implements OfficeService {

  private final PassportRepository passportRepository;
  private final PersonRepository personRepository;
  private final IdGenerator idGenerator;
  private final PassportNumberGenerator passportNumberGenerator;

  @Override
  @NonNull
  public Passport createPassport(
      String personId,
      PassportType type,
      Long number,
      @FutureOrPresent LocalDate givenDate,
      String depCode,
      Status status) {
    validatePassport(personId, type, number);
    String passportId = idGenerator.getId();
    Passport passportModel =
        new Passport(
            passportId,
            personId,
            type,
            number,
            givenDate,
            givenDate.plusYears(type.getValidity()),
            depCode,
            status);
    log.debug("Creating passport [{}]", passportModel);
    return passportRepository.save(passportModel);
  }

  public void validatePassport(String personId, PassportType passportType, Long number) {
    Collection<Passport> passports = passportRepository.findByPersonId(personId);
    Predicate<Passport> passportTypeEquals = passport -> passport.getType().equals(passportType);
    Predicate<Passport> passportStatusEquals = passport -> passport.getStatus().equals(
        Status.ACTIVE);
    Predicate<Passport> passportNumber = passportDto -> passportDto.getNumber().equals(number);

    if (passports.stream().anyMatch(passportTypeEquals.and(passportStatusEquals))) {
      throw new InvalidPassportException("User cannot has two passports with the same type");
    }

    if (passportRepository.getPassports().values().stream().anyMatch(passportNumber)) {
      throw new InvalidPassportException("Passport with such number is already exists.");
    }
  }

  @Override
  public List<Person> getPersonsByFilter(Long passportNumber) {
    log.info("Searching for person by passport number [{}]", passportNumber);
    List<Passport> passports = passportRepository.findByPassportNumber(passportNumber);
    List<Person> persons = new ArrayList<>();
    passports.forEach(
        passport -> persons.add(personRepository.getPersons().get(passport.getPersonId())));
    return persons;
  }

  @SneakyThrows
  @Override
  public Passport updatePassportPerPerson(
      String personId,
      String passportId,
      PassportType type,
      Long number,
      LocalDate givenDate,
      LocalDate expirationDate,
      String departmentCode,
      Status status) {
    log.info("Updating passport [{}] for person with id [{}]", passportId, personId);

    if (status.equals(Status.LOST)) {
      return deactivatePassport(passportId);
    } else {

      return passportRepository.save(
          new Passport(
              passportId,
              personId,
              type,
              number,
              givenDate,
              expirationDate,
              departmentCode,
              status));
    }
  }

  @Override
  public Passport deactivatePassport(String passportId) throws PassportNotFoundException {
    log.info("Deactivate passport with [{}]", passportId);
    Passport passport =
        passportRepository
            .findById(passportId)
            .orElseThrow(() -> new PassportNotFoundException("There is no such passport"));
    log.info("Updated passport [{}]", passport);
    passportRepository.save(
        new Passport(
            passport.getPassportId(),
            passport.getPersonId(),
            passport.getType(),
            passport.getNumber(),
            passport.getGivenDate(),
            passport.getExpirationDate(),
            passport.getDepartmentCode(),
            Status.LOST));

    return createPassport(
        passport.getPersonId(),
        passport.getType(),
        passportNumberGenerator.getNumber(),
        LocalDate.now().plusDays(3),
        passport.getDepartmentCode(),
        Status.ACTIVE);
  }

  @SneakyThrows
  @Override
  public Set<Passport> getPassportsByFilter(
      String personId, LocalDate startDate, LocalDate endDate, String status) {
    log.info(
        "Searching for passport by given date range [{}] - [{}] and status [{}]",
        startDate,
        endDate,
        status);
    List<Passport> passportsPerPerson = passportRepository.findByPersonId(personId);
    return passportRepository.findByFilter(passportsPerPerson, startDate, endDate, status);
  }
}
