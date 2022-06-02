package com.passportoffice.service.impl;

import com.passportoffice.dto.response.PersonDto;
import com.passportoffice.repository.PersonRepository;
import com.passportoffice.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository personRepository;

    private static final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonDto getPerson(Long id) {
        log.info("Searching for person with id [{}]", id);
        return personRepository.findById(id).orElse(new PersonDto());
    }

    @Override
    public PersonDto deletePerson(Long id) {
        log.info("Deleting for person with id [{}]", id);
        return personRepository.deleteById(id).orElseThrow(() -> new NullPointerException("There is no person with id " + id.toString()));
    }

    @Override
    public PersonDto updatePerson(Long id, String firstName, String lastName, LocalDate dateOfBirth, String birthCountry) {
        PersonDto personDto = new PersonDto(id, firstName, lastName, dateOfBirth, birthCountry);
        log.info("Updating for person with id [{}] with data [{}]", id, personDto);
        personRepository.update(id, personDto);
        return getPerson(id);
    }

    @Override
    public PersonDto createPerson(String firstName, String lastName, LocalDate dateOfBirth, String birthCountry) {
        Long id = personRepository.generateId();
        PersonDto personDto = new PersonDto(id, firstName, lastName, dateOfBirth, birthCountry);
        log.info("Creating for person with id [{}] with data [{}]", id, personDto);
        personRepository.save(id, personDto);
        return getPerson(id);
    }
}
