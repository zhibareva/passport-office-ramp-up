package com.passportoffice.dto.request;

import com.passportoffice.controller.validation.PassportStatus;
import com.passportoffice.model.Status;
import com.passportoffice.model.PassportType;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePassportRequest {

  @NotNull private final PassportType type;

  @NotNull private final Long number;

  @NotNull @PastOrPresent private final LocalDate givenDate;

  @NotNull private final String departmentCode;

  @NotNull
  @PassportStatus(acceptedValues={Status.ACTIVE})
  private final Status status;
}
