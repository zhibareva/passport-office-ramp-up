package com.passportoffice.service;

import com.passportoffice.dto.PassportDto;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;

import java.time.LocalDate;

public interface PassportService {

    PassportDto getPassportById(Long id);

    PassportDto deletePassportById(Long id);

    PassportDto updatePassport(Long personId, Long passportID, PassportType type, Long number, LocalDate givenDate,
                               LocalDate expDate, String depCode, Status status);
}
