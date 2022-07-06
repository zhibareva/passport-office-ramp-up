package com.passportoffice.dto.response;

import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;


@Data
@AllArgsConstructor
public class PassportResponse {

    @NotNull
    @Min(value = 1)
    private final Long id;

    @NotNull
    private final PassportType type;

    @NotNull
    private final Long number;

    @PastOrPresent
    private final LocalDate givenDate;

    @FutureOrPresent
    private final LocalDate expirationDate;

    @NotNull
    private final String departmentCode;

    @NotNull
    private final Status status;

}
