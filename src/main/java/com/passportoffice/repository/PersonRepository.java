package com.passportoffice.repository;

import com.passportoffice.model.Person;
import java.util.Map;
import java.util.Optional;

public interface PersonRepository {

  Map<String, Person> getPersons();

  Optional<Person> findById(String id);

  Optional<Person> deleteById(String id);

  Person save(String id, Person passportDto);
}
