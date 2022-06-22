package com.passportoffice.service.impl;

import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.PassportDto;
import com.passportoffice.dto.PersonDto;
import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import com.passportoffice.repository.OfficeRepository;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.service.OfficeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
    private final PassportRepository passportRepository;

    @Override
    public PassportDto createPassport(Long personId, Long passportId, PassportType type, Long number,
                                      LocalDate givenDate,
                                      String depCode, Status status) {
        PassportDto passportDto = new PassportDto(
                passportId, personId, type, number, givenDate, givenDate.plusYears(type.getValidity()), depCode,
                status
        );
        log.info("Creating passport [{}]", passportDto);
        passportRepository.save(passportId, passportDto);
        return passportDto;
    }

    @Override
    public List<PassportDto> getPassportPerPerson(Long personId) {
        log.info("Searching for passport by person id [{}]", personId);
        return officeRepository.findById(personId).orElseThrow(() -> new PassportNotFoundException("There are no passports"));
    }

    @Override
    public List<PersonDto> getPersonsByFilter(Long passportNumber) {
        log.info("Searching for person by passport number [{}]", passportNumber);
        return officeRepository.findByFilter(passportNumber).orElseThrow(
                () -> new NullPointerException("There are no persons with passport number " + passportNumber));
    }

    @Override
    public void updatePassportPerPerson(Long personId, Long passportId, UpdatePassportRequest body) {
        log.info("Updating passport [{}] with data [{}] for person with id [{}]", passportId, personId, body);

        passportRepository.update(passportId, new PassportDto(
                passportId,
                personId,
                body.getType(),
                body.getNumber(),
                body.getGivenDate(),
                body.getExpirationDate(),
                body.getDepartmentCode(),
                body.getStatus()
        ));

        passportRepository.findById(passportId).orElse(new PassportDto());
    }

    @Override
    public Set<PassportDto> getPassportsByFilter(List<PassportDto> filteredPassports, LocalDate startDate,
                                                 LocalDate endDate, String status) {
        log.info("Searching for passport by given date range [{}] - [{}] and status [{}]", startDate, endDate, status);
        return passportRepository.findByFilter(filteredPassports, startDate, endDate, status);
    }
}
