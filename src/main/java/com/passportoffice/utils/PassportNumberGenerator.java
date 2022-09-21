package com.passportoffice.utils;

import org.springframework.stereotype.Component;

@Component
public class PassportNumberGenerator {

  public Long getNumber() {
    long leftLimit = 100000L;
    long rightLimit = 999999L;
    return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
  }
}
