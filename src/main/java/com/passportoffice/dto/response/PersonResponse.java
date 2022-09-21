package com.passportoffice.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;


@Data
@AllArgsConstructor
public class PersonResponse {

  @NonNull private final String id;

  @NonNull private final String firstName;

  @NonNull private final String lastName;

  @NonNull private final LocalDate dateOfBirth;

  @NonNull private final String birthCountry;

}
