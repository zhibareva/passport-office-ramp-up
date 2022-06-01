package com.passportoffice.dto.request;

import com.passportoffice.enums.PassportType;
import com.passportoffice.enums.Status;
import com.passportoffice.validation.PassportNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Validated
@Data
@AllArgsConstructor
public class CreatePassportRequest {

    @NonNull
    private PassportType type;

    @PassportNumber
    @NonNull
    private Long number;

    @NonNull
    @PastOrPresent
    private LocalDate givenDate;

    @NonNull
    private String departmentCode;

    @NonNull
    private Status status;

}
