package com.passportoffice.repository;

import com.passportoffice.dto.PersonDto;

import java.util.Map;
import java.util.Optional;

public interface PersonRepository {

    Long generateId();

    Map<Long, PersonDto> getPersons();

    Optional<PersonDto> findById(Long id);

    Optional<PersonDto> deleteById(Long id);

    void save(Long id, PersonDto passportDto);

    void update(Long id, PersonDto passportDto);
}
