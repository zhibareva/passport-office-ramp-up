package com.passportoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    @NotNull
    private Long id;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String firstName;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String lastName;

    @Past
    private LocalDate dateOfBirth;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String birthCountry;

}
