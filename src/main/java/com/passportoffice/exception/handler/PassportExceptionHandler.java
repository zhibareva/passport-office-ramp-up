package com.passportoffice.exception.handler;

import com.passportoffice.dto.response.ErrorResponse;
import com.passportoffice.exception.InvalidPassportStatusException;
import com.passportoffice.exception.InvalidPassportTypeException;
import com.passportoffice.exception.PassportNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PassportExceptionHandler {
  @ExceptionHandler(PassportNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handlePassportNotFoundException(
      PassportNotFoundException e) {
    ErrorResponse response =
        new ErrorResponse(
            HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidPassportTypeException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseEntity<ErrorResponse> handleInvalidPassportTypeException(
      InvalidPassportTypeException e) {
    ErrorResponse response =
        new ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
            e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(InvalidPassportStatusException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseEntity<ErrorResponse> handleInvalidPassportStatusException(
      InvalidPassportStatusException e) {
    ErrorResponse response =
        new ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
            e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
