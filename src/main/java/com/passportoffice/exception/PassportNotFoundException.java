package com.passportoffice.exception;

public class PassportNotFoundException extends RuntimeException {
  public PassportNotFoundException() {}

  public PassportNotFoundException(String message) {
    super(message);
  }
}