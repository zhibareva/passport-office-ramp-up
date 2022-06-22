package com.passportoffice.service.impl;

import com.passportoffice.dto.PersonDto;
import com.passportoffice.repository.PersonRepository;
import com.passportoffice.service.PersonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public PersonDto getPerson(Long id) {
        log.info("Searching for person with id [{}]", id);
        return personRepository.findById(id).orElse(new PersonDto());
    }

    @Override
    public PersonDto deletePerson(Long id) {
        log.info("Deleting for person with id [{}]", id);
        return personRepository.deleteById(id).orElseThrow(
                () -> new NullPointerException("There is no person with id " + id.toString()));
    }

    @Override
    public PersonDto updatePerson(Long id, String firstName, String lastName, LocalDate dateOfBirth,
                                  String birthCountry) {
        PersonDto personDto = new PersonDto(id, firstName, lastName, dateOfBirth, birthCountry);
        log.info("Updating for person with id [{}] with data [{}]", id, personDto);
        personRepository.update(id, personDto);
        return getPerson(id);
    }

    @Override
    public PersonDto createPerson(Long personId, String firstName, String lastName, LocalDate dateOfBirth, String birthCountry) {

        PersonDto personDto = new PersonDto(personId, firstName, lastName, dateOfBirth, birthCountry);
        log.info("Creating for person with id [{}] with data [{}]", personId, personDto);
        personRepository.save(personId, personDto);
        return getPerson(personId);
    }
}
