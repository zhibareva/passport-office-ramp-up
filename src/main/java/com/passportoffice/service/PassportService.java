package com.passportoffice.service;

import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.enums.PassportType;
import com.passportoffice.enums.Status;

import java.time.LocalDate;

public interface PassportService {
    PassportDto createPassport(PassportType type, Long number, LocalDate givenDate, String depCode, Status status);

    PassportDto getPassportById(Long id);

    PassportDto deletePassportById(Long id);

    PassportDto updatePassport(Long id, PassportType type, Long number, LocalDate givenDate, LocalDate expDate, String depCode, Status status);
}
