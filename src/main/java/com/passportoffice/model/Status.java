package com.passportoffice.model;

public enum Status {
  ACTIVE("active"),

  INACTIVE("inActive"),

  LOST("lost");

  private final String value;

  Status(String value) {
    this.value = value;
  }

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
