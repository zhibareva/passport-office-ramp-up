package com.passportoffice;

import com.passportoffice.controller.PassportsApiController;
import com.passportoffice.controller.PersonsApiController;
import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.dto.request.CreatePersonRequest;
import com.passportoffice.dto.response.PassportResponse;
import com.passportoffice.dto.response.PersonResponse;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import com.passportoffice.utils.PassportNumberGenerator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PassportApiControllerTest {

  @Autowired
  private final PassportNumberGenerator passportNumberGenerator;
  private final DataGenerator dataGenerator;
  private final PassportsApiController passportsApiController;
  private final PersonsApiController personsApiController;

  @LocalServerPort int port;

  @BeforeEach
  void init() {
    RestAssured.baseURI = "http://localhost/v1/";
    RestAssured.port = port;
  }

  @ParameterizedTest
  @EnumSource(PassportType.class)
  void testPassportDelete(PassportType passportType) {

    PersonResponse personResponse =
        personsApiController.createPerson(
            new CreatePersonRequest(
                dataGenerator.getName(),
                dataGenerator.getLastName(),
                dataGenerator
                    .getCurrentDate()
                    .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
                dataGenerator.getCountry()));
    PassportResponse passportResponse =
        personsApiController.addPassport(
            personResponse.getId(),
            new CreatePassportRequest(
                passportType,
                passportNumberGenerator.getNumber(),
                dataGenerator.getCurrentDate(),
                "123-321",
                Status.ACTIVE));
    RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .when()
        .request(Method.DELETE, "/passports/" + passportResponse.getId())
        .then()
        .statusCode(200);
  }

  @ParameterizedTest
  @EnumSource(PassportType.class)
  void testPassportGet(PassportType passportType) {
    PersonResponse personResponse =
        personsApiController.createPerson(
            new CreatePersonRequest(
                dataGenerator.getName(),
                dataGenerator.getLastName(),
                dataGenerator
                    .getCurrentDate()
                    .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
                dataGenerator.getCountry()));
    PassportResponse passportResponse =
        personsApiController.addPassport(
            personResponse.getId(),
            new CreatePassportRequest(
                passportType,
                passportNumberGenerator.getNumber(),
                dataGenerator.getCurrentDate(),
                "123-321",
                Status.ACTIVE));
    RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .when()
        .request(Method.GET, "/passports/" + passportResponse.getId())
        .then()
        .statusCode(200);
  }

  @Test
  void testPassportGetNotFound() {
    RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .when()
        .request(Method.GET, "/passports/123")
        .then()
        .statusCode(404);
  }

  @ParameterizedTest
  @EnumSource(PassportType.class)
  void testPassportDeactivate(PassportType passportType) {
    PersonResponse personResponse =
        personsApiController.createPerson(
            new CreatePersonRequest(
                dataGenerator.getName(),
                dataGenerator.getLastName(),
                dataGenerator
                    .getCurrentDate()
                    .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
                dataGenerator.getCountry()));
    PassportResponse passportResponse =
        personsApiController.addPassport(
            personResponse.getId(),
            new CreatePassportRequest(
                passportType,
                passportNumberGenerator.getNumber(),
                dataGenerator.getCurrentDate(),
                "123-321",
                Status.ACTIVE));
    PassportResponse generatedPassport = passportsApiController.deactivatePassport(passportResponse.getId());
    Assertions.assertNotNull(generatedPassport.getId(), "id should be not null");
    Assertions.assertEquals(
        passportResponse.getType(),
        generatedPassport.getType(),
        "type should be equals");
    Assertions.assertEquals(
        passportResponse.getDepartmentCode(),
        generatedPassport.getDepartmentCode(),
        "code should be equals");
  }
}
