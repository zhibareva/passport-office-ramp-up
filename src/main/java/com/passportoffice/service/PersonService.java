package com.passportoffice.service;

import com.passportoffice.dto.PersonDto;

import java.time.LocalDate;

public interface PersonService {
    PersonDto getPerson(String id);

    PersonDto deletePerson(String id);

    PersonDto updatePerson(String id, String firstName, String lastName, LocalDate dateOfBirth, String birthCountry);

    PersonDto createPerson(String firstName, String lastName, LocalDate dateOfBirth, String birthCountry);
}
