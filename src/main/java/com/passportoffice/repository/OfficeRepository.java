package com.passportoffice.repository;

import com.passportoffice.dto.PassportDto;
import com.passportoffice.dto.PersonDto;

import java.util.List;
import java.util.Optional;

public interface OfficeRepository {

    Optional<List<PassportDto>> findById(Long personId);

    Optional<List<PersonDto>> findByFilter(Long passportNumber);
}
