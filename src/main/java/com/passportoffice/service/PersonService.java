package com.passportoffice.service;

import com.passportoffice.model.Person;
import java.time.LocalDate;

public interface PersonService {
  Person getPerson(String id);

  Person deletePerson(String id);

  Person updatePerson(
      String id, String firstName, String lastName, LocalDate dateOfBirth, String birthCountry);

  Person createPerson(
      String firstName, String lastName, LocalDate dateOfBirth, String birthCountry);
}
