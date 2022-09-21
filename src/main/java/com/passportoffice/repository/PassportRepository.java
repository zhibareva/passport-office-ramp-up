package com.passportoffice.repository;

import com.passportoffice.model.Passport;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface PassportRepository {

  Optional<Passport> deleteById(String id);

  Passport save(Passport passportDto);

  Optional<Passport> findById(String id);

  Set<Passport> findByStatus(Set<Passport> passports, String status);

  Set<Passport> findByStartDate(Set<Passport> passports, LocalDate startDate);

  Set<Passport> findByEndDate(Set<Passport> passports, LocalDate endDate);

  Set<Passport> findByFilter(
      List<Passport> filteredPassports, LocalDate startDate,LocalDate endDate, String status);

  Map<String, Passport> getPassports();

  List<Passport> findByPersonId(String personId);

  List<Passport> findByPassportNumber(Long passportNumber);
}
