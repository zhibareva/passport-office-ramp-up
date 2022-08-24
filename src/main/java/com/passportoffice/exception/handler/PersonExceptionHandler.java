package com.passportoffice.exception.handler;

import com.passportoffice.dto.response.ErrorResponse;
import com.passportoffice.exception.PersonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PersonExceptionHandler {
  @ExceptionHandler(PersonNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handlePersonNotFoundException(PersonNotFoundException e) {
    ErrorResponse response =
        new ErrorResponse(
            HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
}
