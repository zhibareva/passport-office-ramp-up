package com.passportoffice.repository.impl;

import com.passportoffice.model.Passport;
import com.passportoffice.model.Status;
import com.passportoffice.repository.PassportRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class PassportRepositoryImpl implements PassportRepository {

  private final Map<String, Passport> passports = new HashMap<>();

  @Override
  public Map<String, Passport> getPassports() {
    return Collections.unmodifiableMap(passports);
  }

  @Override
  public Optional<Passport> deleteById(String id) {
    return Optional.ofNullable(passports.remove(id));
  }

  @Override
  public Passport save(Passport passportDto) {
    passports.put(passportDto.getPassportId(), passportDto);
    return passportDto;
  }

  @Override
  public Optional<Passport> findById(String id) {
    return Optional.ofNullable(passports.get(id));
  }

  @Override
  public Set<Passport> findByStatus(Set<Passport> passports, String status) {
    return passports.stream()
        .filter(passportDto -> passportDto.getStatus().equals(Status.fromValue(status)))
        .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public Set<Passport> findByStartDate(Set<Passport> passports, LocalDate startDate) {
    return passports.stream()
        .filter(passportDto -> passportDto.getGivenDate().isAfter(startDate))
        .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public Set<Passport> findByEndDate(Set<Passport> passports, LocalDate endDate) {
    return passports.stream()
        .filter(passportDto -> passportDto.getGivenDate().isBefore(endDate))
        .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public Set<Passport> findByFilter(
      List<Passport> passportDtos, LocalDate startDate, LocalDate endDate, String status) {
    Set<Passport> filteredPassports;
    Set<Passport> buffer = new HashSet<>(passportDtos);

    log.trace("Passports before filtering");
    buffer.forEach(passport -> log.trace("[{}]", passport.toString()));

    if (startDate != null) {
      filteredPassports = new HashSet<>(findByStartDate(buffer, startDate));
      buffer = new HashSet<>(filteredPassports);
      log.trace("Passports after filtering by start date");
      filteredPassports.forEach(passport -> log.trace("[{}]", passport.toString()));
    }

    if (endDate != null) {
      filteredPassports = new HashSet<>(findByEndDate(buffer, endDate));
      buffer = new HashSet<>(filteredPassports);
      log.trace("Passports after filtering by end date");
      filteredPassports.forEach(passport -> log.trace("[{}]", passport.toString()));
    }

    if (status != null) {
      filteredPassports = new HashSet<>(findByStatus(buffer, status));
      buffer = new HashSet<>(filteredPassports);
      log.trace("Passports after filtering by status");
      filteredPassports.forEach(passport -> log.trace("[{}]", passport.toString()));
    }

    filteredPassports = new HashSet<>(buffer);

    log.trace("Passports after filtering");
    filteredPassports.forEach(passport -> log.trace("[{}]", passport.toString()));
    return Collections.unmodifiableSet(filteredPassports);
  }

  @Override
  public List<Passport> findByPersonId(String personId) {
    return getPassports().values().stream()
        .filter(passportDto -> passportDto.getPersonId().equals(personId))
        .collect(Collectors.toUnmodifiableList());
  }

  @Override
  public List<Passport> findByPassportNumber(Long passportNumber) {
    return getPassports().values().stream()
        .filter(passportDto -> passportDto.getNumber().equals(passportNumber))
        .collect(Collectors.toUnmodifiableList());
  }
}
