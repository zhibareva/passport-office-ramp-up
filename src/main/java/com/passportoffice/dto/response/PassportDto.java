package com.passportoffice.dto.response;

import com.passportoffice.enums.PassportType;
import com.passportoffice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassportDto {

    @NotNull
    @Min(value = 1)
    private Long id = 123L;

    @NotNull
    private PassportType type;

    @NotNull
    private Long number;

    @PastOrPresent
    private LocalDate givenDate;

    @FutureOrPresent
    private LocalDate expirationDate;

    @NotNull
    private String departmentCode;

    @NotNull
    private Status status;

}
