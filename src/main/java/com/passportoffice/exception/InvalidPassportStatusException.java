package com.passportoffice.exception;

public class InvalidPassportStatusException extends RuntimeException {
  public InvalidPassportStatusException() {}

  public InvalidPassportStatusException(String message) {
    super(message);
  }
}
