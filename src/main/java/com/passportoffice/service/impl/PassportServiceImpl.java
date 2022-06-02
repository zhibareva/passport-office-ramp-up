package com.passportoffice.service.impl;

import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.enums.PassportType;
import com.passportoffice.enums.Status;
import com.passportoffice.exception.InvalidPassportTypeException;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.repository.impl.PassportRepositoryImpl;
import com.passportoffice.service.PassportService;
import com.passportoffice.validation.validator.PassportTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PassportServiceImpl implements PassportService {

    @Autowired
    private PassportRepository passportRepository;
    private static final Logger log = LoggerFactory.getLogger(PassportServiceImpl.class);

    public PassportServiceImpl(PassportRepositoryImpl passportRepository) {
        this.passportRepository = passportRepository;
    }

    @Override
    public PassportDto createPassport(PassportType type, Long number, LocalDate givenDate, String depCode, Status status) {
        Long id  = passportRepository.generateId();
        PassportDto passportDto = new PassportDto(id, type, number, givenDate, givenDate.plusYears(type.getValidity()), depCode, status);
        log.info("Creating passport with metadata [{}]", passportDto);
        passportRepository.save(id, passportDto);
        return passportDto;
    }

    @Override
    public PassportDto getPassportById(Long id) {
        log.info("Searching for passport with id [{}]", id);
        return passportRepository.findById(id).orElse(new PassportDto());
    }

    @Override
    public PassportDto deletePassportById(Long id) {
        log.info("Deleting passport with id [{}]", id);
        return passportRepository.deleteById(id).orElseThrow(() -> new NullPointerException("There is passport with id"));
    }

    @Override
    public PassportDto updatePassport(Long id, PassportType type, Long number, LocalDate givenDate, LocalDate expDate, String depCode, Status status) {
        log.info("Updating passport with metadata [{}]", id);
        passportRepository.update(id, new PassportDto(id, type, number, givenDate, expDate, depCode, status));
        return getPassportById(id);
    }

}
