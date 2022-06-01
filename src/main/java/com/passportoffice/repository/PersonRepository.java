package com.passportoffice.repository;

import com.passportoffice.dto.response.PersonDto;

import java.util.Optional;

public interface PersonRepository {
    Optional<PersonDto> findById(Long id);

    Optional<PersonDto> deleteById(Long id);

    void save(Long id, PersonDto passportDto);

    void update(Long id, PersonDto passportDto);

    Long generateId();
}
