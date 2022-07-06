package com.passportoffice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@AllArgsConstructor
public class Passport {

    private final Long passportId;
    private final Long personId;
    private final PassportType type;
    private final Long number;
    private final LocalDate givenDate;
    private final LocalDate expirationDate;
    private final String departmentCode;
    private final Status status;

}
