package com.passportoffice.utils;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataGenerator {

  public static Long generatePassportNumber() {
    long leftLimit = 100000L;
    long rightLimit = 999999L;
    return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
  }

  public static LocalDate getCurrentDate() {
    return LocalDate.now();
  }

  public static String getName(String nameType) {
    return nameType + RandomStringUtils.randomAlphabetic(10);
  }
}
