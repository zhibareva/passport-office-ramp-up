package com.passportoffice.controller.validation.validator;

import com.passportoffice.controller.validation.PassportStatus;
import com.passportoffice.model.Status;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<PassportStatus, Status> {
  private List<String> valueList;

  @Override
  public void initialize(PassportStatus constraintAnnotation) {
    valueList = new ArrayList<>();
    for (Status val : constraintAnnotation.acceptedValues()) {
      valueList.add(val.toString());
    }
  }

  @Override
  public boolean isValid(Status value, ConstraintValidatorContext context) {
    return valueList.contains(value.toString());
  }
}
