package com.passportoffice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ErrorResponse {

  @NonNull private final int status;

  @NonNull private final String error;

  @NonNull private final String message;
}
