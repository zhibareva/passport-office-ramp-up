package com.passportoffice.utils;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public final class DataGenerator {

  public LocalDate getCurrentDate() {
    return LocalDate.now();
  }

  public String getName() {
    return new Faker().name().firstName();
  }

  public String getLastName() {
    return new Faker().name().lastName();
  }

  public String getCountry() {
    return new Faker().country().name();
  }
}
