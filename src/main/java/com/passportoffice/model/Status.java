package com.passportoffice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Status {
  ACTIVE("active"),
  INACTIVE("inActive"),
  LOST("lost");

  @Getter
  private final String value;

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static Status fromValue(String value) {
    for (Status b : Status.values()) {
      if (String.valueOf(b.value).equals(value)) {
        return b;
      }
    }
    return null;
  }
}
