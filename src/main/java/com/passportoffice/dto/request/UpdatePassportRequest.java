package com.passportoffice.dto.request;

import com.passportoffice.enums.PassportType;
import com.passportoffice.enums.Status;
import com.passportoffice.validation.PassportNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePassportRequest {

  @NonNull
  private PassportType type;

  @NonNull
  @PassportNumber
  private Long number;

  @NonNull
  @PastOrPresent
  private LocalDate givenDate;

  @FutureOrPresent
  private LocalDate expirationDate;

  @NonNull
  private String departmentCode;

  @NonNull
  private Status status;

}
