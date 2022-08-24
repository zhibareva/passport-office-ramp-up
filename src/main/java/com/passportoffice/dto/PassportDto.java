package com.passportoffice.dto;

import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PassportDto {

  private final String passportId;
  private final PassportType type;
  private final Long number;
  private final LocalDate givenDate;
  @FutureOrPresent private final LocalDate expirationDate;
  private final String departmentCode;
  private final Status status;
}
