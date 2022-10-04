package com.passportoffice;

import com.passportoffice.controller.PassportsApiController;
import com.passportoffice.controller.PersonsApiController;
import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.dto.request.CreatePersonRequest;
import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.request.UpdatePersonRequest;
import com.passportoffice.dto.response.PassportResponse;
import com.passportoffice.dto.response.PersonResponse;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import com.passportoffice.utils.PassportNumberGenerator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.json.JSONArray;
import org.json.JSONException;
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

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PersonApiControllerTest {

  @Autowired
  private final PassportNumberGenerator passportNumberGenerator;
  private final DataGenerator dataGenerator;
  private final PassportsApiController passportsApiController;
  private final PersonsApiController personsApiController;

  @LocalServerPort
  int port;

  @BeforeEach
  void init() {
    RestAssured.baseURI = "http://localhost/v1/";
    RestAssured.port = port;
  }

  @Test
  void testPersonPost() {
    PersonResponse personResponse =
        personsApiController.createPerson(
            new CreatePersonRequest(
                dataGenerator.getName(),
                dataGenerator.getLastName(),
                dataGenerator
                    .getCurrentDate()
                    .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
                dataGenerator.getCountry()));

    Assertions.assertNotNull(personResponse.getId(), "Id should be not null");
    Assertions.assertNotNull(personResponse.getFirstName(), "firstName should be not null");
    Assertions.assertNotNull(personResponse.getLastName(), "lastName should be not null");
    Assertions.assertNotNull(personResponse.getDateOfBirth(), "dateOfBirth should be not null");
    Assertions.assertNotNull(personResponse.getBirthCountry(), "birthCountry should be not null");
  }

  @Test
  void testPersonGet() {
    String id = personsApiController.createPerson(
        new CreatePersonRequest(
            dataGenerator.getName(),
            dataGenerator.getLastName(),
            dataGenerator
                .getCurrentDate()
                .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
            dataGenerator.getCountry())).getId();
    RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .when()
        .request(Method.GET, "/persons/{id}", id)
        .then()
        .statusCode(200);
  }

  @Test
  void testPersonGetByFilter() {
    String id = personsApiController.createPerson(
        new CreatePersonRequest(
            dataGenerator.getName(),
            dataGenerator.getLastName(),
            dataGenerator
                .getCurrentDate()
                .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
            dataGenerator.getCountry())).getId();
    PassportResponse passportResponse =
        personsApiController.addPassport(
            id,
            new CreatePassportRequest(
                PassportType.CITIZEN,
                passportNumberGenerator.getNumber(),
                dataGenerator.getCurrentDate(),
                "123-321",
                Status.ACTIVE));

    RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .when()
        .request(Method.GET, "/persons?passportNumber=" + passportResponse.getNumber())
        .then()
        .statusCode(200);
    Response list =
        RestAssured.given()
            .header(HttpHeaders.ACCEPT, ContentType.JSON)
            .when()
            .request(Method.GET, "/persons?passportNumber=" + 113)
            .then()
            .statusCode(200)
            .extract()
            .response();
    Assertions.assertEquals("[]", list.asString());
  }

  @Test
  void testPersonPut() {
    String id = personsApiController.createPerson(
        new CreatePersonRequest(
            dataGenerator.getName(),
            dataGenerator.getLastName(),
            dataGenerator
                .getCurrentDate()
                .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
            dataGenerator.getCountry())).getId();
    LocalDate newDateOfBirth =
        dataGenerator
            .getCurrentDate()
            .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2)));
    Response response =
        RestAssured.given()
            .header(HttpHeaders.ACCEPT, ContentType.JSON)
            .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
            .and()
            .body(new UpdatePersonRequest("John", "Smith", newDateOfBirth, "Serbia"))
            .when()
            .put("/persons/" + id)
            .then()
            .extract()
            .response();

    Assertions.assertEquals(200, response.statusCode());
    Assertions.assertEquals("John", response.jsonPath().getString("firstName"));
    Assertions.assertEquals("Smith", response.jsonPath().getString("lastName"));
  }

  @ParameterizedTest
  @EnumSource(PassportType.class)
  void testPassportPost(PassportType passportType) {
    String id = personsApiController.createPerson(
        new CreatePersonRequest(
            dataGenerator.getName(),
            dataGenerator.getLastName(),
            dataGenerator
                .getCurrentDate()
                .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
            dataGenerator.getCountry())).getId();
    PassportResponse passportResponse =
        personsApiController.addPassport(
            id,
            new CreatePassportRequest(
                passportType,
                passportNumberGenerator.getNumber(),
                dataGenerator.getCurrentDate(),
                "123-321",
                Status.ACTIVE));

    Assertions.assertEquals("ACTIVE", passportResponse.getStatus().toString());
    Assertions.assertEquals(passportType.toString(), passportResponse.getType().toString());
  }

  @Test
  void testPassportGetByFilter() throws JSONException {
    String id = personsApiController.createPerson(
        new CreatePersonRequest(
            dataGenerator.getName(),
            dataGenerator.getLastName(),
            dataGenerator
                .getCurrentDate()
                .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
            dataGenerator.getCountry())).getId();
    List<PassportType> passportTypes = Arrays.asList(PassportType.values());
    passportTypes.forEach(passportType -> personsApiController.addPassport(
        id,
        new CreatePassportRequest(
            passportType,
            passportNumberGenerator.getNumber(),
            dataGenerator.getCurrentDate(),
            "123-321",
            Status.ACTIVE)));

    Response passportActive =
        RestAssured.given()
            .header(HttpHeaders.ACCEPT, ContentType.JSON)
            .when()
            .request(Method.GET, "/persons/{id}/passports?status=active", id)
            .then()
            .extract()
            .response();
    Assertions.assertEquals(200, passportActive.statusCode());
    Assertions.assertTrue(passportActive.jsonPath().getString("status").contains("ACTIVE"));
    Assertions.assertEquals(3, new JSONArray(passportActive.body().print()).length());

    Response passportInActive =
        RestAssured.given()
            .header(HttpHeaders.ACCEPT, ContentType.JSON)
            .when()
            .request(Method.GET, "/persons/{id}/passports?status=inActive", id)
            .then()
            .extract()
            .response();
    Assertions.assertEquals(200, passportInActive.statusCode());
    Assertions.assertEquals(0, new JSONArray(passportInActive.body().print()).length());

    Response passportsByStartDate =
        RestAssured.given()
            .header(HttpHeaders.ACCEPT, ContentType.JSON)
            .when()
            .request(Method.GET, "/persons/{id}/passports?startDate=2017-08-04", id)
            .then()
            .extract()
            .response();
    Assertions.assertEquals(200, passportsByStartDate.statusCode());
    Assertions.assertEquals(3, new JSONArray(passportsByStartDate.body().print()).length());

    Response passportsByEndDate =
        RestAssured.given()
            .header(HttpHeaders.ACCEPT, ContentType.JSON)
            .when()
            .request(Method.GET, "/persons/{id}/passports?endDate=2017-08-04", id)
            .then()
            .extract()
            .response();
    Assertions.assertEquals(200, passportsByEndDate.statusCode());
    Assertions.assertEquals(0, new JSONArray(passportsByEndDate.body().print()).length());
  }

  @Test
  void testPassportPutInvalidExpirationDate() {
    String personId = personsApiController.createPerson(
        new CreatePersonRequest(
            dataGenerator.getName(),
            dataGenerator.getLastName(),
            dataGenerator
                .getCurrentDate()
                .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
            dataGenerator.getCountry())).getId();
    PassportResponse passportResponse =
        personsApiController.addPassport(
            personId,
            new CreatePassportRequest(
                PassportType.CITIZEN,
                passportNumberGenerator.getNumber(),
                dataGenerator.getCurrentDate(),
                "123-321",
                Status.ACTIVE));
    String passportId = passportResponse.getId();

    Assertions.assertEquals("ACTIVE", passportResponse.getStatus().toString());

    Response errorResponse =
        RestAssured.given()
            .header(HttpHeaders.ACCEPT, ContentType.JSON)
            .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
            .and()
            .body(
                new UpdatePassportRequest(
                    PassportType.SAILOR,
                    passportNumberGenerator.getNumber(),
                    dataGenerator.getCurrentDate(),
                    dataGenerator.getCurrentDate().minusYears(2),
                    "123-132",
                    Status.ACTIVE))
            .when()
            .put("/persons/{personId}/passports/{passportId}", personId, passportId)
            .then()
            .extract()
            .response();

    Assertions.assertEquals(404, errorResponse.statusCode());
  }

  @Test
  void testPassportPut() {
    String personId = personsApiController.createPerson(
        new CreatePersonRequest(
            dataGenerator.getName(),
            dataGenerator.getLastName(),
            dataGenerator
                .getCurrentDate()
                .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
            dataGenerator.getCountry())).getId();
    PassportResponse passportResponse =
        personsApiController.addPassport(
            personId,
            new CreatePassportRequest(
                PassportType.CITIZEN,
                passportNumberGenerator.getNumber(),
                dataGenerator.getCurrentDate(),
                "123-321",
                Status.ACTIVE));
    String passportId = passportResponse.getId();

    Assertions.assertEquals("ACTIVE", passportResponse.getStatus().toString());

    Response response =
        RestAssured.given()
            .header(HttpHeaders.ACCEPT, ContentType.JSON)
            .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
            .and()
            .body(
                new UpdatePassportRequest(
                    PassportType.CITIZEN,
                    passportNumberGenerator.getNumber(),
                    dataGenerator.getCurrentDate(),
                    dataGenerator.getCurrentDate().plusYears(2),
                    "123-132",
                    Status.ACTIVE))
            .when()
            .put("/persons/{personId}/passports/{passportId}", personId, passportId)
            .then()
            .extract()
            .response();

    Assertions.assertEquals(200, response.statusCode());
  }

  @Test
  void testPassportPutDeactivate() {
    String personId = personsApiController.createPerson(
        new CreatePersonRequest(
            dataGenerator.getName(),
            dataGenerator.getLastName(),
            dataGenerator
                .getCurrentDate()
                .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
            dataGenerator.getCountry())).getId();
    PassportResponse passportResponse =
        personsApiController.addPassport(
            personId,
            new CreatePassportRequest(
                PassportType.CITIZEN,
                passportNumberGenerator.getNumber(),
                dataGenerator.getCurrentDate(),
                "123-321",
                Status.ACTIVE));
    String passportId = passportResponse.getId();

    Assertions.assertEquals("ACTIVE", passportResponse.getStatus().toString());

    Response response =
        RestAssured.given()
            .header(HttpHeaders.ACCEPT, ContentType.JSON)
            .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
            .and()
            .body(
                new UpdatePassportRequest(
                    PassportType.CITIZEN,
                    passportNumberGenerator.getNumber(),
                    dataGenerator.getCurrentDate(),
                    dataGenerator.getCurrentDate().plusYears(2),
                    "123-132",
                    Status.LOST))
            .when()
            .put("/persons/{personId}/passports/{passportId}", personId, passportId)
            .then()
            .extract()
            .response();

    Assertions.assertEquals(200, response.statusCode());
    Assertions.assertNotEquals(passportId, response.jsonPath().getString("id"));
  }

  @Test
  void testPassportDelete() {
    String id = personsApiController.createPerson(
        new CreatePersonRequest(
            dataGenerator.getName(),
            dataGenerator.getLastName(),
            dataGenerator
                .getCurrentDate()
                .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2))),
            dataGenerator.getCountry())).getId();
    RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .when()
        .request(Method.DELETE, "/persons/" + id)
        .then()
        .statusCode(200);
  }
}
