package com.passportoffice.service.impl;

import com.passportoffice.controller.mapper.PersonEntitiesMapper;
import com.passportoffice.dto.PersonDto;
import com.passportoffice.exception.PersonNotFoundException;
import com.passportoffice.model.Person;
import com.passportoffice.repository.PersonRepository;
import com.devskiller.friendly_id.FriendlyId;
import com.passportoffice.service.PersonService;
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
  private final PersonEntitiesMapper personEntitiesMapper;

  @SneakyThrows
  @Override
  @NonNull
  public PersonDto getPerson(String id) {
    log.info("Searching for person with id [{}]", id);
    Person person =
        personRepository
            .findById(id)
            .orElseThrow(() -> new PersonNotFoundException("There is no person with such id"));
    return personEntitiesMapper.toDto(person);
  }

  @Override
  @NonNull
  public PersonDto deletePerson(String id) {
    log.info("Deleting for person with id [{}]", id);
    Person person =
        personRepository
            .deleteById(id)
            .orElseThrow(() -> new PersonNotFoundException("There is no person with such id"));
    return personEntitiesMapper.toDto(person);
  }

  @Override
  @NonNull
  public PersonDto updatePerson(
      String id, String firstName, String lastName, LocalDate dateOfBirth, String birthCountry) {
    Person personModel = new Person(id, firstName, lastName, dateOfBirth, birthCountry);
    log.info("Updating for person with id [{}] with data [{}]", id, personModel);
    Person person = personRepository.save(id, personModel);
    return personEntitiesMapper.toDto(person);
  }

  @Override
  @NonNull
  public PersonDto createPerson(
      String firstName, String lastName, LocalDate dateOfBirth, String birthCountry) {
    String personId = FriendlyId.createFriendlyId();
    Person personModel = new Person(personId, firstName, lastName, dateOfBirth, birthCountry);
    log.info("Creating for person with id [{}] with data [{}]", personId, personModel.toString());
    Person person = personRepository.save(personId, personModel);
    return personEntitiesMapper.toDto(person);
  }
}
