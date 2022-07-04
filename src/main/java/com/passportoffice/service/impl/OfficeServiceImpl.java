package com.passportoffice.service.impl;

import com.passportoffice.controller.PassportsApiController;
import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.PassportDto;
import com.passportoffice.dto.PersonDto;
import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import com.passportoffice.repository.OfficeRepository;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.service.OfficeService;
import com.passportoffice.utils.DataGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<PassportDto> getPassportPerPerson(Long personId) throws PassportNotFoundException {
        log.info("Searching for passport by person id [{}]", personId);

        return officeRepository.findById(personId).orElseThrow(
                () -> new PassportNotFoundException("There are no passports"));
    }

    @Override
    public List<PersonDto> getPersonsByFilter(Long passportNumber) {
        log.info("Searching for person by passport number [{}]", passportNumber);
        return officeRepository.findByFilter(passportNumber).filter(List::isEmpty).orElseThrow(
                () -> new IllegalStateException("There are no persons with passport number " + passportNumber));
    }

    @Override
    public void updatePassportPerPerson(Long personId, Long passportId, UpdatePassportRequest body) {
        log.info("Updating passport [{}] with data [{}] for person with id [{}]", passportId, personId, body);

        if (body.getStatus().equals(Status.LOST)) {
            deactivatePassport(passportId);
        } else {
            passportRepository.save(passportId, new PassportDto(
                    passportId,
                    personId,
                    body.getType(),
                    body.getNumber(),
                    body.getGivenDate(),
                    body.getExpirationDate(),
                    body.getDepartmentCode(),
                    body.getStatus()
            ));
        }

        PassportDto updatedPassport = passportRepository.findById(passportId).orElseThrow(() -> new RuntimeException());

    }

    @Override
    public PassportDto deactivatePassport(Long passportId) {
        log.info("deactivate passport [{}]", passportId);
        PassportDto passportDto = passportRepository.findById(passportId).orElseThrow(RuntimeException::new);
        log.info("Updated passport [{}]", passportDto);
        passportRepository.save(passportId, new PassportDto(
                passportDto.getPassportId(),
                passportDto.getPersonId(),
                passportDto.getType(),
                passportDto.getNumber(),
                passportDto.getGivenDate(),
                passportDto.getExpirationDate(),
                passportDto.getDepartmentCode(),
                Status.LOST
        ));
        Long newId = passportRepository.generateId();
        createPassport(
                passportDto.getPersonId(),
                newId,
                passportDto.getType(),
                DataGenerator.generatePassportNumber(),
                DataGenerator.getCurrentDate().plusDays(3),
                passportDto.getDepartmentCode(),
                Status.ACTIVE
        );
        return passportRepository.findById(passportId).orElseThrow(RuntimeException::new);
    }

    @Override
    public Set<PassportDto> getPassportsByFilter(List<PassportDto> filteredPassports, LocalDate startDate,
                                                 LocalDate endDate, String status) {
        log.info("Searching for passport by given date range [{}] - [{}] and status [{}]", startDate, endDate, status);
        return passportRepository.findByFilter(filteredPassports, startDate, endDate, status);
    }
}
