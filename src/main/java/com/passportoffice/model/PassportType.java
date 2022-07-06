package com.passportoffice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PassportType {
    CITIZEN("citizen", 15),
    INTERNATIONAL("international", 10),
    SAILOR("sailor", 5);

    public int getValidity() {
        return validity;
    }

    private String value;
    private int validity;

    PassportType(String value, int validity) {
        this.value = value;
        this.validity = validity;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static PassportType fromValue(String text) {
        for (PassportType b : PassportType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
