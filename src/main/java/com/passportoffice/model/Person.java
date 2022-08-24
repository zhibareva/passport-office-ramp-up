package com.passportoffice.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Person {
  private final String id;
  private final String firstName;
  private final String lastName;
  private final LocalDate dateOfBirth;
  private final String birthCountry;
}
