package com.passportoffice.repository.impl;

import com.passportoffice.dto.response.PassportDto;
import com.passportoffice.enums.Status;
import com.passportoffice.repository.PassportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PassportRepositoryImpl implements PassportRepository {
    @Override
    public Map<Long, PassportDto> getPassports() {
        return passports;
    }

    private final Map<Long, PassportDto> passports = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(PassportRepositoryImpl.class);

    @Override
    public Long generateId() {
        return passports.size() + 1L;
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
    public void update(Long id, PassportDto passportDto) {
        passports.replace(id, passportDto);
    }
    
    @Override
    public Optional<PassportDto> findById(Long id) {
        return Optional.of(passports.get(id));
    }
    
    @Override
    public List<PassportDto> findByStatus(List<PassportDto> passports, String status) {
        return passports.stream().filter(
                PassportDto -> PassportDto.getStatus().equals(Status.fromValue(status))
        ).collect(Collectors.toList());
    }

    @Override
    public List<PassportDto> findByStartDate(List<PassportDto> passports, LocalDate startDate) {
        return passports.stream().filter(
                PassportDto -> PassportDto.getGivenDate().isAfter(startDate)
        ).collect(Collectors.toList());
    }

    @Override
    public List<PassportDto> findByEndDate(List<PassportDto> passports, LocalDate endDate) {
        return passports.stream().filter(
                PassportDto -> PassportDto.getGivenDate().isBefore(endDate)
        ).collect(Collectors.toList());
    }

    @Override
    public List<PassportDto> findByFilter(List<PassportDto> passportDtos, LocalDate startDate, LocalDate endDate, String status) {
        List<PassportDto> filteredPassports = new ArrayList<>();
        if (startDate != null) {
            filteredPassports.addAll(findByStartDate(passportDtos, startDate));
            log.info("startDate [{}]", filteredPassports);
        }
        if (endDate != null) {
            filteredPassports.addAll(findByEndDate(passportDtos, endDate));
            log.info("endDate [{}]", filteredPassports);
        }
        if (status != null) {
            filteredPassports.addAll(findByStatus(passportDtos, status));
            log.info("status [{}]", filteredPassports);
        }

        return filteredPassports;
    }
}
