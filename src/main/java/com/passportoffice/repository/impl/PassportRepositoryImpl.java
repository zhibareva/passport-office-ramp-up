package com.passportoffice.repository.impl;

import com.passportoffice.dto.PassportDto;
import com.passportoffice.model.Status;
import com.passportoffice.repository.PassportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class PassportRepositoryImpl implements PassportRepository {

    private final Map<Long, PassportDto> passports = new HashMap<>();

    @Override
    public Map<Long, PassportDto> getPassports() {
        return Collections.unmodifiableMap(passports);
    }

    @Override
    public Long generateId() {
        return getPassports().size() + 1L;
    }

    @Override
    public Optional<PassportDto> deleteById(Long id) {
        return Optional.of(passports.remove(id));
    }

    @Override
    public void save(Long id, PassportDto passportDto) {
        passports.put(id, passportDto);
    }

    @Override
    public Optional<PassportDto> findById(Long id) {
        return Optional.of(passports.get(id));
    }

    @Override
    public Set<PassportDto> findByStatus(List<PassportDto> passports, String status) {
        return passports.stream().filter(
                passportDto -> passportDto.getStatus().equals(Status.fromValue(status))
        ).collect(Collectors.toSet());
    }

    @Override
    public Set<PassportDto> findByStartDate(List<PassportDto> passports, LocalDate startDate) {
        return passports.stream().filter(
                passportDto -> passportDto.getGivenDate().isAfter(startDate)
        ).collect(Collectors.toSet());
    }

    @Override
    public Set<PassportDto> findByEndDate(List<PassportDto> passports, LocalDate endDate) {
        return passports.stream().filter(
                passportDto -> passportDto.getGivenDate().isBefore(endDate)
        ).collect(Collectors.toSet());
    }

    @Override
    public Set<PassportDto> findByFilter(List<PassportDto> passportDtos, LocalDate startDate, LocalDate endDate,
                                         String status) {
        Set<PassportDto> filteredPassports = new HashSet<>();
        if (startDate != null) {
            filteredPassports.addAll(findByStartDate(passportDtos, startDate));
            log.info("Passports after filtering by start date [{}]", filteredPassports);
        }
        if (endDate != null) {
            filteredPassports.addAll(findByEndDate(passportDtos, endDate));
            log.info("Passports after filtering by end date [{}]", filteredPassports);
        }
        if (status != null) {
            filteredPassports.addAll(findByStatus(passportDtos, status));
            log.info("Passports after filtering by status [{}]", filteredPassports);
        }

        return filteredPassports;
    }
}
