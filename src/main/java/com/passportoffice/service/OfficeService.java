package com.passportoffice.service;

import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.dto.response.PersonDto;

import java.time.LocalDate;
import java.util.List;

public interface OfficeService {
    void addPassportToPerson(Long personId, PassportDto passportDto);

    List<PassportDto> getPassportPerPerson(String personId);

    List<PersonDto> getPersonsByFilter(Long passportNumber);

    void updatePassportPerPerson(Long personId, Long passportId, UpdatePassportRequest body);

    List<PassportDto> getPassportsByFilter(List<PassportDto> filteredPassports, LocalDate startDate, LocalDate endDate, String status);
}
