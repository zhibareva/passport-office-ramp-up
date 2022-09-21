package com.passportoffice.service.impl;

import com.passportoffice.exception.PersonNotFoundException;
import com.passportoffice.model.Person;
import com.passportoffice.repository.PersonRepository;
import com.passportoffice.service.PersonService;
import com.passportoffice.utils.IdGenerator;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

  private final PersonRepository personRepository;
  private final IdGenerator idGenerator;

  @SneakyThrows
  @Override
  @NonNull
  public Person getPerson(String id) {
    log.info("Searching for person with id [{}]", id);
    return personRepository
        .findById(id)
        .orElseThrow(() -> new PersonNotFoundException("There is no person with such id"));
  }

  @Override
  @NonNull
  public Person deletePerson(String id) {
    log.info("Deleting for person with id [{}]", id);
    return personRepository
        .deleteById(id)
        .orElseThrow(() -> new PersonNotFoundException("There is no person with such id"));
  }

  @Override
  @NonNull
  public Person updatePerson(
      String id, String firstName, String lastName, LocalDate dateOfBirth, String birthCountry) {
    Person personModel = new Person(id, firstName, lastName, dateOfBirth, birthCountry);
    log.info("Updating for person with id [{}] with data [{}]", id, personModel);
    return personRepository.save(id, personModel);
  }

  @Override
  @NonNull
  public Person createPerson(
      String firstName, String lastName, LocalDate dateOfBirth, String birthCountry) {
    String personId = idGenerator.getId();
    Person personModel = new Person(personId, firstName, lastName, dateOfBirth, birthCountry);
    log.info("Creating for person with id [{}] with data [{}]", personId, personModel);
    return personRepository.save(personId, personModel);
  }
}
