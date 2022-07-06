package com.passportoffice.service;

import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.PassportDto;
import com.passportoffice.dto.PersonDto;
import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface OfficeService {

    PassportDto createPassport(Long personId, Long passportId, PassportType type, Long number, LocalDate givenDate,
                               String depCode, Status status);

    List<PassportDto> getPassportPerPerson(Long personId) throws PassportNotFoundException;

    List<PersonDto> getPersonsByFilter(Long passportNumber);

    void updatePassportPerPerson(Long personId, Long passportId, UpdatePassportRequest body);

    PassportDto deactivatePassport(Long passportId);

    Set<PassportDto> getPassportsByFilter(List<PassportDto> filteredPassports, LocalDate startDate, LocalDate endDate,
                                          String status);
}
