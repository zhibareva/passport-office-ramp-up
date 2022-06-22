package com.passportoffice.repository.impl;

import com.passportoffice.dto.PersonDto;
import com.passportoffice.repository.PersonRepository;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final Map<Long, PersonDto> persons = new HashMap<>();

    @Override
    public Long generateId() {
        return getPersons().size() + 1L;
    }

    public Map<Long, PersonDto> getPersons() {
        return Collections.unmodifiableMap(persons);
    }

    @Override
    public Optional<PersonDto> findById(Long id) {
        return Optional.of(persons.get(id));
    }

    @Override
    public Optional<PersonDto> deleteById(Long id) {
        return Optional.of(persons.remove(id));
    }

    @Override
    public void save(Long id, PersonDto passportDto) {
        persons.put(id, passportDto);
    }

    @Override
    public void update(Long id, PersonDto passportDto) {
        persons.replace(id, passportDto);
    }



}
