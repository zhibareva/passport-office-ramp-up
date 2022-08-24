package com.passportoffice.exception;

public class InvalidPassportTypeException extends RuntimeException {
  public InvalidPassportTypeException() {}

  public InvalidPassportTypeException(String message) {
    super(message);
  }
}
