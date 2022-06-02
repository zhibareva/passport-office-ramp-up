package com.passportoffice.service.impl;

import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.dto.response.PersonDto;
import com.passportoffice.repository.OfficeRepository;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.repository.impl.OfficeRepositoryImpl;
import com.passportoffice.repository.impl.PassportRepositoryImpl;
import com.passportoffice.service.OfficeService;
import com.passportoffice.service.PassportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OfficeServiceImpl implements OfficeService {

    private static final Logger log = LoggerFactory.getLogger(OfficeServiceImpl.class);
    private final OfficeRepository officeRepository;
    private final PassportRepository passportRepository;
    private final PassportService passportService;

    public OfficeServiceImpl(OfficeRepositoryImpl officeRepository,
                             PassportRepositoryImpl passportRepository,
                             PassportServiceImpl passportService) {
        this.officeRepository = officeRepository;
        this.passportRepository = passportRepository;
        this.passportService = passportService;
    }

    @Override
    public void addPassportToPerson(Long personId, PassportDto passportDto) {
        log.info("Creating passport with data [{}] for person with id [{}]", passportDto, personId);
        officeRepository.save(personId, passportDto);
    }

    @Override
    public List<PassportDto> getPassportPerPerson(String personId) {
        log.info("Searching for passport by person id [{}]", personId);
        return officeRepository.findById(personId).orElse(new ArrayList<>());
    }

    @Override
    public List<PersonDto> getPersonsByFilter(Long passportNumber) {
        log.info("Searching for person by passport number [{}]", passportNumber);
        return officeRepository.findByFilter(passportNumber).orElseThrow(() -> new NullPointerException("There are no persons with passport number " + passportNumber));
    }

    @Override
    public void updatePassportPerPerson(Long personId, Long passportId, UpdatePassportRequest body) {
        log.info("Updating passport [{}] with data [{}] for person with id [{}]", passportId, personId, body);

        passportRepository.update(passportId, new PassportDto(
                passportId,
                body.getType(),
                body.getNumber(),
                body.getGivenDate(),
                body.getExpirationDate(),
                body.getDepartmentCode(),
                body.getStatus()
        ));

        PassportDto passportDto = passportRepository.findById(passportId).orElse(new PassportDto());
        officeRepository.update(personId, passportId, passportDto);
    }

    @Override
    public List<PassportDto> getPassportsByFilter(List<PassportDto> filteredPassports, LocalDate startDate, LocalDate endDate, String status) {
        log.info("Searching for passport by given date range [{}] - [{}] and status [{}]", startDate, endDate, status);
        return passportRepository.findByFilter(filteredPassports, startDate, endDate, status);
    }
}
