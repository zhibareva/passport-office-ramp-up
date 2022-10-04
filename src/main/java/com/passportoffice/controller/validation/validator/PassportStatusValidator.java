package com.passportoffice.controller.validation.validator;

import com.passportoffice.controller.validation.PassportStatus;
import com.passportoffice.model.Status;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PassportStatusValidator implements ConstraintValidator<PassportStatus, Status> {
  private List<String> valueList;

  @Override
  public void initialize(PassportStatus constraintAnnotation) {
    valueList =
        Arrays.stream(constraintAnnotation.acceptedValues())
            .map(Status::toString)
            .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(Status value, ConstraintValidatorContext context) {
    return valueList.contains(value.toString());
  }
}
