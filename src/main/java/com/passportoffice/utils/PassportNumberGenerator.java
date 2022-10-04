package com.passportoffice.utils;

import org.springframework.stereotype.Component;

@Component
public class PassportNumberGenerator {
  private static final long LEFT_LIMIT = 100000L;
  private static final long RIGHT_LIMIT = 999999L;

  public Long getNumber() {
    return LEFT_LIMIT + (long) (Math.random() * (RIGHT_LIMIT - LEFT_LIMIT));
  }
}
