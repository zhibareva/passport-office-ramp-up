package com.passportoffice.dto.request;

import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import validation.ElementOfSubset;
import validation.ElementOfEnum;
import validation.PassportNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;


@Data
@AllArgsConstructor
public class CreatePassportRequest {

    @NonNull
    private final PassportType type;

    @NonNull
    @PassportNumber
    private final Long number;

    @NonNull
    @PastOrPresent
    private final LocalDate givenDate;

    @NonNull
    private final String departmentCode;

    @NonNull
    @ElementOfSubset(anyOf = {Status.ACTIVE})
    private final Status status;

}
