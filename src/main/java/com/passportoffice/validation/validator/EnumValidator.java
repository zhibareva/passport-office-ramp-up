package com.passportoffice.validation.validator;

import com.passportoffice.exception.InvalidPassportStatusException;
import com.passportoffice.model.Status;
import com.passportoffice.validation.PassportStatus;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<PassportStatus, Object> {
  private Set<String> acceptedValues;
  private Status[] subset;

  @Override
  public void initialize(PassportStatus annotation) {
    acceptedValues =
        Stream.of(annotation.enumClass().getEnumConstants())
            .map(Enum::name)
            .collect(Collectors.toSet());
    this.subset = annotation.anyOf();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    if (value.getClass() == String.class && Arrays.asList(subset).contains(value))
      return true;
    else if (value.getClass() == Status.class && acceptedValues.contains(((Status) value).name()))
      return true;
    else throw new InvalidPassportStatusException("Invalid passport status found");
  }
}
