package com.passportoffice.service;

import com.passportoffice.exception.PassportNotFoundException;
import com.passportoffice.model.Passport;

public interface PassportService {

    Passport getPassportById(String id) throws PassportNotFoundException;

    Passport deletePassportById(String id) throws PassportNotFoundException;
}
