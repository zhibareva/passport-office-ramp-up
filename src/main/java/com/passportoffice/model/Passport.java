package com.passportoffice.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Passport {

    private final String passportId;
    private final String personId;
    private final PassportType type;
    private final Long number;
    private final LocalDate givenDate;
    private final LocalDate expirationDate;
    private final String departmentCode;
    private final Status status;

}
