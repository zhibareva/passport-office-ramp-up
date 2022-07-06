package com.passportoffice.repository.impl;

import com.passportoffice.dto.PassportDto;
import com.passportoffice.dto.PersonDto;
import com.passportoffice.dto.response.PersonResponse;
import com.passportoffice.repository.OfficeRepository;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class OfficeRepositoryImpl implements OfficeRepository {

    private final PassportRepository passportRepository;
    private final PersonRepository personRepository;

    @Override
    public Optional<List<PassportDto>> findById(Long personId) {
        return Optional.of(passportRepository.getPassports().values()
                                   .stream()
                                   .filter(
                                           passportDto -> passportDto.getPersonId().equals(personId))
                                   .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<PersonDto>> findByFilter(Long passportNumber) {
        List<PassportDto> passports = passportRepository.getPassports().values()
                .stream()
                .filter(
                        passportDto -> passportDto.getNumber().equals(passportNumber))
                .collect(Collectors.toList());
        List<PersonDto> persons = new ArrayList<>();
        passports.forEach(passportDto -> persons.add(personRepository.getPersons().get(passportDto.getPersonId())));
        return Optional.of(persons);
    }


}
