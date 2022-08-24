package com.passportoffice.dto.request;

import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePersonRequest {

  @NotEmpty
  @Pattern(regexp = "^[a-zA-Z\\s]+$")
  private final String firstName;

  @NotEmpty
  @Pattern(regexp = "^[a-zA-Z\\s]+$")
  private final String lastName;

  @Past private final LocalDate dateOfBirth;

  @NotEmpty
  @Pattern(regexp = "^[a-zA-Z\\s]+$")
  private String birthCountry;

  @Override
  public String toString() {
    return "\nCreatePersonRequest{" +
        "\n\tfirstName='" + firstName + '\'' +
        ", \n\tlastName='" + lastName + '\'' +
        ", \n\tdateOfBirth=" + dateOfBirth +
        ", \n\tbirthCountry='" + birthCountry + '\'' +
        '}';
  }
}
