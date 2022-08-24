package com.passportoffice.repository;

import com.passportoffice.model.Passport;
import com.passportoffice.model.Person;
import java.util.List;

public interface OfficeRepository {

  List<Passport> findById(String personId);

  List<Person> findByFilter(Long passportNumber);
}
