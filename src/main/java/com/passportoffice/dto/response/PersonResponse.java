package com.passportoffice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;


@Data
@AllArgsConstructor
public class PersonResponse {

    @NotNull
    private final Long id;

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
    private final String birthCountry;

}
