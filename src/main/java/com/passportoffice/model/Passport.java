package com.passportoffice.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Passport {

    private final String passportId;
    private final String personId;
    private final PassportType type;
    private final Long number;
    private final LocalDate givenDate;
    private final LocalDate expirationDate;
    private final String departmentCode;
    private final Status status;

    @Override
    public String toString() {
        return "\nPassport{" +
            "\n\tpassportId='" + passportId + '\'' +
            ", \n\tpersonId='" + personId + '\'' +
            ", \n\ttype=" + type +
            ", \n\tnumber=" + number +
            ", \n\tgivenDate=" + givenDate +
            ", \n\texpirationDate=" + expirationDate +
            ", \n\tdepartmentCode='" + departmentCode + '\'' +
            ", \n\tstatus=" + status +
            '}';
    }
}
