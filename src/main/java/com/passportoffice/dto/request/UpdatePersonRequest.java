package com.passportoffice.dto.request;

import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class UpdatePersonRequest {

  @NotEmpty
  @Pattern(regexp = "^[a-zA-Z\\s]+$")
  private final String firstName;

  @NotEmpty
  @Pattern(regexp = "^[a-zA-Z\\s]+$")
  private final String lastName;

  @NonNull @Past private final LocalDate dateOfBirth;

  @NotEmpty
  @Pattern(regexp = "^[a-zA-Z\\s]+$")
  private final String birthCountry;
}
