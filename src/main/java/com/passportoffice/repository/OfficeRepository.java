package com.passportoffice.repository;

import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.dto.response.PersonDto;

import java.util.List;
import java.util.Optional;

public interface OfficeRepository {
    void save(Long personId, PassportDto passportDto);

    Optional<List<PassportDto>> findById(String personId);

    Optional<List<PersonDto>> findByFilter(Long passportNumber);

    void update(Long personId, Long passportId, PassportDto updatedPassport);
}
