package com.passportoffice.repository;

import com.passportoffice.dto.response.PassportDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PassportRepository {

    Long generateId();

    Optional<PassportDto> deleteById(Long id);

    void save(Long id, PassportDto passportDto);

    void update(Long id, PassportDto passportDto);

    Optional<PassportDto> findById(Long id);

    List<PassportDto> findByStatus(List<PassportDto> passports, String status);

    List<PassportDto> findByStartDate(List<PassportDto> passports, LocalDate startDate);

    List<PassportDto> findByEndDate(List<PassportDto> passports, LocalDate endDate);

    List<PassportDto> findByFilter(List<PassportDto> filteredPassports, LocalDate startDate, LocalDate endDate, String status);

    Map<Long, PassportDto> getPassports();
}
