package com.passportoffice.service.impl;

import com.devskiller.friendly_id.FriendlyId;
import com.passportoffice.controller.mapper.PassportEntitiesMapper;
import com.passportoffice.controller.mapper.PersonEntitiesMapper;
import com.passportoffice.dto.PassportDto;
import com.passportoffice.dto.PersonDto;
import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.exception.InvalidPassportTypeException;
import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.exception.PersonNotFoundException;
import com.passportoffice.model.Passport;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Person;
import com.passportoffice.model.Status;
import com.passportoffice.repository.OfficeRepository;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.service.OfficeService;
import com.passportoffice.utils.DataGenerator;
import com.passportoffice.validation.PassportStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OfficeServiceImpl implements OfficeService {

  private final OfficeRepository officeRepository;
  private final PassportRepository passportRepository;
  private final PassportEntitiesMapper passportEntitiesMapper;
  private final PersonEntitiesMapper personEntitiesMapper;

  @Override
  public PassportDto createPassport(
      String personId,
      PassportType type,
      Long number,
      LocalDate givenDate,
      String depCode,
      Status status) {
    validatePassportType(personId, type);
    String passportId = FriendlyId.createFriendlyId();
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
    Passport passport = passportRepository.save(passportModel);
    return passportEntitiesMapper.toDto(passport);
  }

  public void validatePassportType(String personId, PassportType passportType) {
    Collection<Passport> passportDtos = officeRepository.findById(personId);
    if (passportDtos.stream().anyMatch(passport -> passport.getType().equals(passportType))
        && passportDtos.stream().anyMatch(passport -> passport.getStatus().equals(Status.ACTIVE))) {

      throw new InvalidPassportTypeException("User cannot has two passports with the same type");
    }
  }

  @SneakyThrows
  @Override
  public List<PassportDto> getPassportPerPerson(String personId) {
    log.info("Searching for passport by person id [{}]", personId);

    List<Passport> passports = officeRepository.findById(personId);
    if (passports.isEmpty())
      throw new PassportNotFoundException("There is no passports with such id");
    List<PassportDto> passportDtos = new ArrayList<>();
    passports.forEach(passport -> passportDtos.add(passportEntitiesMapper.toDto(passport)));
    return passportDtos;
  }

  @Override
  public List<PersonDto> getPersonsByFilter(Long passportNumber) {
    log.info("Searching for person by passport number [{}]", passportNumber);
    List<Person> persons = officeRepository.findByFilter(passportNumber);
    if (persons.isEmpty())
      throw new PersonNotFoundException(
          "There are no persons with passport number " + passportNumber);
    List<PersonDto> personDtos = new ArrayList<>();
    persons.forEach(person -> personDtos.add(personEntitiesMapper.toDto(person)));
    return personDtos;
  }

  @SneakyThrows
  @Override
  public PassportDto updatePassportPerPerson(
      String personId, String passportId, UpdatePassportRequest body) {
    log.info(
        "Updating passport [{}] with data [{}] for person with id [{}]",
        passportId,
        personId,
        body);

    PassportDto passportDto;
    if (body.getStatus().equals(Status.LOST)) {
      passportDto = deactivatePassport(passportId);
    } else {
      Passport passport =
          passportRepository.save(
              new Passport(
                  passportId,
                  personId,
                  body.getType(),
                  body.getNumber(),
                  body.getGivenDate(),
                  body.getExpirationDate(),
                  body.getDepartmentCode(),
                  body.getStatus()));
      passportDto = passportEntitiesMapper.toDto(passport);
    }

    return passportDto;
  }

  @Override
  public PassportDto deactivatePassport(String passportId) {
    log.info("Deactivate passport with [{}]", passportId);
    Passport passportDto =
        passportRepository.findById(passportId).orElseThrow(RuntimeException::new);
    log.info("Updated passport [{}]", passportDto);
    passportRepository.save(
        new Passport(
            passportDto.getPassportId(),
            passportDto.getPersonId(),
            passportDto.getType(),
            passportDto.getNumber(),
            passportDto.getGivenDate(),
            passportDto.getExpirationDate(),
            passportDto.getDepartmentCode(),
            Status.LOST));

    return createPassport(
        passportDto.getPersonId(),
        passportDto.getType(),
        DataGenerator.generatePassportNumber(),
        DataGenerator.getCurrentDate().plusDays(3),
        passportDto.getDepartmentCode(),
        Status.ACTIVE);
  }

  @SneakyThrows
  @Override
  public Set<PassportDto> getPassportsByFilter(
      String personId, LocalDate startDate, LocalDate endDate, String status) {
    log.info(
        "Searching for passport by given date range [{}] - [{}] and status [{}]",
        startDate,
        endDate,
        status);
    List<Passport> passportsPerPerson = officeRepository.findById(personId);
    if (passportsPerPerson.isEmpty())
      throw new PassportNotFoundException("There is no passports with such id");

    Set<Passport> passports =
        passportRepository.findByFilter(passportsPerPerson, startDate, endDate, status);
    Set<PassportDto> passportDtos = new HashSet<>();
    passports.forEach(passport -> passportDtos.add(passportEntitiesMapper.toDto(passport)));
    return passportDtos;
  }
}
