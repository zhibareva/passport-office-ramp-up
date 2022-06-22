package com.passportoffice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Validated
@Data
@AllArgsConstructor
public class CreatePersonRequest {

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private final String firstName;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private final String lastName;

    @Past
    private final LocalDate dateOfBirth;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String birthCountry;

}
