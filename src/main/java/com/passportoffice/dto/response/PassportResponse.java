package com.passportoffice.dto.response;

import com.passportoffice.model.Status;
import com.passportoffice.model.PassportType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class PassportResponse {

  @NonNull private final String id;

  @NonNull private final PassportType type;

  @NonNull private final Long number;

  @NonNull private final LocalDate givenDate;

  @NonNull private final LocalDate expirationDate;

  @NonNull private final String departmentCode;

  @NonNull private final Status status;
}
