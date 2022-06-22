package com.passportoffice.service.impl;

import com.passportoffice.dto.PassportDto;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import com.passportoffice.repository.PassportRepository;
import com.passportoffice.service.PassportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Slf4j
public class PassportServiceImpl implements PassportService {

    private final PassportRepository passportRepository;

    @Override
    public PassportDto getPassportById(Long passportId) {
        log.info("Searching for passport with passportId [{}]", passportId);
        return passportRepository.findById(passportId).orElse(new PassportDto());
    }

    @Override
    public PassportDto deletePassportById(Long passportId) {
        log.info("Deleting passport with passportId [{}]", passportId);
        return passportRepository.deleteById(passportId).orElseThrow(
                () -> new NullPointerException("There is passport with passportId"));
    }

    @Override
    public PassportDto updatePassport(Long personId, Long passportId, PassportType type, Long number,
                                      LocalDate givenDate, LocalDate expDate, String depCode, Status status) {
        log.info("Updating passport with metadata [{}]", passportId);
        passportRepository.update(
                passportId, new PassportDto(passportId, personId, type, number, givenDate, expDate, depCode, status));
        return getPassportById(passportId);
    }

}
