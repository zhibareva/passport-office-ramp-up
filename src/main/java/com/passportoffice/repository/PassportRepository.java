package com.passportoffice.repository;

import com.passportoffice.dto.PassportDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface PassportRepository {

    Long generateId();

    Optional<PassportDto> deleteById(Long id);

    void save(Long id, PassportDto passportDto);

    void update(Long id, PassportDto passportDto);

    Optional<PassportDto> findById(Long id);

    Set<PassportDto> findByStatus(List<PassportDto> passports, String status);

    Set<PassportDto> findByStartDate(List<PassportDto> passports, LocalDate startDate);

    Set<PassportDto> findByEndDate(List<PassportDto> passports, LocalDate endDate);

    Set<PassportDto> findByFilter(List<PassportDto> filteredPassports, LocalDate startDate, LocalDate endDate, String status);

    Map<Long, PassportDto> getPassports();
}
