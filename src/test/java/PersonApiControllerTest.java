import com.passportoffice.Application;
import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.dto.request.CreatePersonRequest;
import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.request.UpdatePersonRequest;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import com.passportoffice.utils.DataGenerator;
import com.passportoffice.utils.PassportNumberGenerator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = Application.class)
class PersonApiControllerTest {

  private DataGenerator dataGenerator;
  private PassportNumberGenerator passportNumberGenerator;

  @BeforeEach
  void init() {
    RestAssured.baseURI = "http://localhost/v1/";
    RestAssured.port = 7070;
    dataGenerator = new DataGenerator();
    passportNumberGenerator = new PassportNumberGenerator();
  }

  public Response createTestPassport(String personId, PassportType passportType, Status status) {
    return RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
        .and()
        .body(
            new CreatePassportRequest(
                passportType,
                passportNumberGenerator.getNumber(),
                dataGenerator.getCurrentDate(),
                "123-321",
                status))
        .when()
        .post("/persons/{personId}/passports", personId)
        .then()
        .extract()
        .response();
  }

  public Response createTestPerson() {
    String firstName = dataGenerator.getName();
    String lastName = dataGenerator.getLastName();
    LocalDate dateOfBirth =
        dataGenerator
            .getCurrentDate()
            .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2)));

    return RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
        .and()
        .body(new CreatePersonRequest(firstName, lastName, dateOfBirth, dataGenerator.getCountry()))
        .when()
        .post("/persons")
        .then()
        .extract()
        .response();
  }

  @Test
  void testPersonPost() {
    Response response = createTestPerson();
    Assertions.assertEquals(200, response.statusCode());
    Assertions.assertNotNull(response.jsonPath().getString("id"), "Id should be not null");
    Assertions.assertNotNull(
        response.jsonPath().getString("firstName"), "firstName should be not null");
    Assertions.assertNotNull(
        response.jsonPath().getString("lastName"), "lastName should be not null");
    Assertions.assertNotNull(
        response.jsonPath().getString("dateOfBirth"), "dateOfBirth should be not null");
    Assertions.assertNotNull(
        response.jsonPath().getString("birthCountry"), "birthCountry should be not null");
  }

  @Test
  void testPersonGet() {
    String id = createTestPerson().jsonPath().getString("id");
    RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .when()
        .request(Method.GET, "/persons/{id}", id)
        .then()
        .statusCode(200);
  }

  @Test
  void testPersonGetByFilter() {
    String id = createTestPerson().jsonPath().getString("id");
    Response passport = createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);
    RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .when()
        .request(Method.GET, "/persons?passportNumber=" + passport.jsonPath().getString("number"))
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
    String id = createTestPerson().jsonPath().getString("id");
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

  @Test
  void testPersonDelete() {
    String id = createTestPerson().jsonPath().getString("id");
    RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .when()
        .request("DELETE", "/persons/{id}", id)
        .then()
        .statusCode(204);
  }

  @ParameterizedTest
  @EnumSource(PassportType.class)
  void testPassportPost(PassportType passportType) {
    String id = createTestPerson().jsonPath().getString("id");
    Response response = createTestPassport(id, passportType, Status.ACTIVE);

    Assertions.assertEquals(200, response.statusCode());
    Assertions.assertEquals("ACTIVE", response.jsonPath().getString("status"));
    Assertions.assertEquals(passportType.toString(), response.jsonPath().getString("type"));
  }

  @Test
  void testPassportGetByFilter() throws JSONException {
    String id = createTestPerson().jsonPath().getString("id");
    createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);
    createTestPassport(id, PassportType.SAILOR, Status.ACTIVE);
    createTestPassport(id, PassportType.INTERNATIONAL, Status.ACTIVE);

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
    String personId = createTestPerson().jsonPath().getString("id");
    Response createResponse = createTestPassport(personId, PassportType.CITIZEN, Status.ACTIVE);
    String passportId = createResponse.jsonPath().getString("id");

    Assertions.assertEquals(200, createResponse.statusCode());
    Assertions.assertEquals("ACTIVE", createResponse.jsonPath().getString("status"));

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
    String personId = createTestPerson().jsonPath().getString("id");
    Response createResponse = createTestPassport(personId, PassportType.CITIZEN, Status.ACTIVE);
    String passportId = createResponse.jsonPath().getString("id");

    Assertions.assertEquals(200, createResponse.statusCode());
    Assertions.assertEquals("ACTIVE", createResponse.jsonPath().getString("status"));

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
    String personId = createTestPerson().jsonPath().getString("id");
    Response createResponse = createTestPassport(personId, PassportType.CITIZEN, Status.ACTIVE);
    String passportId = createResponse.jsonPath().getString("id");

    Assertions.assertEquals(200, createResponse.statusCode());
    Assertions.assertEquals("ACTIVE", createResponse.jsonPath().getString("status"));

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
    String id = createTestPerson().jsonPath().getString("id");
    RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .when()
        .request(Method.DELETE, "/persons/" + id)
        .then()
        .statusCode(200);
  }
}
