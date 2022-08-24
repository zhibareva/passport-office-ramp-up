package com.passportoffice.dto.request;

import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import com.passportoffice.validation.PassportNumber;
import com.passportoffice.validation.PassportStatus;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePassportRequest {

  @NotNull private final PassportType type;

  @NotNull @PassportNumber private final Long number;

  @NotNull @PastOrPresent private final LocalDate givenDate;

  @NotNull private final String departmentCode;

  @NotNull
  @PassportStatus(
      anyOf = {Status.ACTIVE},
      enumClass = Status.class)
  private final Status status;
}
