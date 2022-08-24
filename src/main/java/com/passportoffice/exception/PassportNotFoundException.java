package com.passportoffice.exception;

public class PassportNotFoundException extends Exception {
  public PassportNotFoundException() {}

  public PassportNotFoundException(String message) {
    super(message);
  }
}