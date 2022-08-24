package com.passportoffice.repository.impl;

import com.passportoffice.model.Passport;
import com.passportoffice.model.Person;
import com.passportoffice.repository.OfficeRepository;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.repository.PersonRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class OfficeRepositoryImpl implements OfficeRepository {

  private final PassportRepository passportRepository;
  private final PersonRepository personRepository;

  @Override
  public List<Passport> findById(String personId) {
    return passportRepository.getPassports().values().stream()
        .filter(passportDto -> passportDto.getPersonId().equals(personId))
        .collect(Collectors.toList());
  }

  @Override
  public List<Person> findByFilter(Long passportNumber) {
    List<Passport> passports =
        passportRepository.getPassports().values().stream()
            .filter(passportDto -> passportDto.getNumber().equals(passportNumber))
            .collect(Collectors.toList());
    List<Person> persons = new ArrayList<>();
    passports.forEach(
        passportDto -> persons.add(personRepository.getPersons().get(passportDto.getPersonId())));
    return persons;
  }
}
