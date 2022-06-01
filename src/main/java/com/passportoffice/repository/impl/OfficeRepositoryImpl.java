package com.passportoffice.repository.impl;

import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.dto.response.PersonDto;
import com.passportoffice.repository.OfficeRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OfficeRepositoryImpl implements OfficeRepository {

    private final Map<PersonDto, Map<Long, PassportDto>> officeRepo = new HashMap<>();

    @Override
    public void save(Long personId, PassportDto passportDto) {
        for (Map.Entry<PersonDto, Map<Long, PassportDto>> passportsPerPerson : officeRepo.entrySet()) {
            if (passportsPerPerson.getKey().getId().equals(personId)) {
                passportsPerPerson.getValue().put(passportDto.getId(), passportDto);
            }
        }
    }

    @Override
    public Optional<List<PassportDto>> findById(String personId) {
        List<PassportDto> passportDtos = new ArrayList<>();
        for (Map.Entry<PersonDto, Map<Long, PassportDto>> passportsPerPerson : officeRepo.entrySet()) {
            if (passportsPerPerson.getKey().getId().equals(Long.parseLong(personId))) {
                passportDtos = new ArrayList<>(passportsPerPerson.getValue().values());
            }
        }
        return Optional.of(passportDtos);
    }

    @Override
    public Optional<List<PersonDto>> findByFilter(Long passportNumber) {
        List<PersonDto> persons = new ArrayList<>();
        for (Map.Entry<PersonDto, Map<Long, PassportDto>> passportsPerPerson : officeRepo.entrySet()) {
            for (Map.Entry<Long, PassportDto> passports : passportsPerPerson.getValue().entrySet()) {
                if (passports.getValue().getNumber().equals(passportNumber)) {
                    persons.add(passportsPerPerson.getKey());
                }
            }
        }
        return Optional.of(persons);
    }

    @Override
    public void update(Long personId, Long passportId, PassportDto updatedPassport) {

        for (Map.Entry<PersonDto, Map<Long, PassportDto>> passportsPerPerson : officeRepo.entrySet()) {
            if (passportsPerPerson.getKey().getId().equals(personId)) {
                passportsPerPerson.getValue().replace(passportId, updatedPassport);
            }
        }
    }
}
