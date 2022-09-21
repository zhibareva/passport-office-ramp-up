package com.passportoffice.exception;

public class InvalidPassportException extends RuntimeException {
  public InvalidPassportException() {}

  public InvalidPassportException(String message) {
    super(message);
  }
}
