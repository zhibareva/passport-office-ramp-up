package com.passportoffice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;


@Data
@AllArgsConstructor
public class ErrorResponse {

    private final HttpStatus status;
    private final String error;
    private final String message;

}
