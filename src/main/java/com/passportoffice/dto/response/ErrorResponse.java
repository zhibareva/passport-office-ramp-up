package com.passportoffice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@AllArgsConstructor
public class ErrorResponse {

    private HttpStatus status;
    private String error;
    private String message;

}
