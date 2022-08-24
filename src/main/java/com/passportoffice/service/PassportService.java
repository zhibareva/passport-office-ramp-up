package com.passportoffice.service;

import com.passportoffice.dto.PassportDto;
import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import java.time.LocalDate;

public interface PassportService {

    PassportDto getPassportById(String id) throws PassportNotFoundException;

    PassportDto deletePassportById(String id) throws PassportNotFoundException;

    PassportDto updatePassport(String personId, String passportID, PassportType type, Long number, LocalDate givenDate,
                               LocalDate expDate, String depCode, Status status);
}
