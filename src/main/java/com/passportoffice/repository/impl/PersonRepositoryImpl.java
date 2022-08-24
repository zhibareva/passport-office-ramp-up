package com.passportoffice.repository.impl;

import com.passportoffice.model.Person;
import com.passportoffice.repository.PersonRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

  private final Map<String, Person> persons = new HashMap<>();

  public Map<String, Person> getPersons() {
    return Collections.unmodifiableMap(persons);
  }

  @Override
  public Optional<Person> findById(String id) {
    return Optional.ofNullable(persons.get(id));
  }

  @Override
  public Optional<Person> deleteById(String id) {
    return Optional.of(persons.remove(id));
  }

  @Override
  public Person save(String id, Person passportDto) {
    persons.put(id, passportDto);
    return passportDto;
  }
}
