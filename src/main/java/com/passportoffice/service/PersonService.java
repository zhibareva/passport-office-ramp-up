package com.passportoffice.service;

import com.passportoffice.dto.response.PersonDto;

import java.time.LocalDate;

public interface PersonService {
    PersonDto getPerson(Long id);

    PersonDto deletePerson(Long id);

    PersonDto updatePerson(Long id, String firstName, String lastName, LocalDate dateOfBirth, String birthCountry);

    PersonDto createPerson(String firstName, String lastName, LocalDate dateOfBirth, String birthCountry);
}
