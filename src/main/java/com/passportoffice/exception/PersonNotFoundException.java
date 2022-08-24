package com.passportoffice.exception;

public class PersonNotFoundException extends RuntimeException {
  public PersonNotFoundException() {}

  public PersonNotFoundException(String message) {
    super(message);
  }
}