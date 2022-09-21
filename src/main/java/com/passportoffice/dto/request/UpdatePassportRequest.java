package com.passportoffice.dto.request;

import com.passportoffice.controller.validation.PassportStatus;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePassportRequest {

  @NotNull private final PassportType type;

  @NotNull private final Long number;

  @NotNull @PastOrPresent private final LocalDate givenDate;

  @NotNull @FutureOrPresent private final LocalDate expirationDate;

  @NotNull private final String departmentCode;

  @NotNull
  @PassportStatus(acceptedValues={Status.ACTIVE, Status.LOST})
  private final Status status;
}
