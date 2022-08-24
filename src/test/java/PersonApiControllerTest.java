import com.passportoffice.Application;
import com.passportoffice.dto.request.CreatePersonRequest;
import com.passportoffice.dto.request.UpdatePassportRequest;
import com.passportoffice.dto.request.UpdatePersonRequest;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import com.passportoffice.utils.DataGenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.io.IOException;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
class PersonApiControllerTest {

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = "http://localhost/v1/";
    RestAssured.port = 7070;
  }

  static void cleanUp() throws IOException {
    Process process = Runtime.getRuntime().exec("netstat -ano | findstr :7070");
    Runtime.getRuntime().exec("taskkill /PID " + process.pid() + " /F");
  }

  @Test
  void generateTestData() {
    for (int i = 0; i <= 20; i++) testPassportPost();
  }

  static Response createTestPerson() {
    String firstName = DataGenerator.getName("firstName");
    String lastName = DataGenerator.getName("lastName");
    LocalDate dateOfBirth =
        DataGenerator.getCurrentDate()
            .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2)));

    return RestAssured.given()
        .header("Accept", "application/json")
        .header("Content-type", "application/json")
        .and()
        .body(new CreatePersonRequest(firstName, lastName, dateOfBirth, "Russia"))
        .when()
        .post("/persons")
        .then()
        .extract()
        .response();
  }

  @Test
  void testPersonPost() {

    Response response = createTestPerson();

    Assertions.assertEquals(201, response.statusCode());
    Assertions.assertNotNull(response.jsonPath().getString("id"), "Id should be not null");
    Assertions.assertNotNull(
        response.jsonPath().getString("firstName"), "firstName should be not null");
    Assertions.assertNotNull(
        response.jsonPath().getString("lastName"), "lastName should be not null");
    Assertions.assertNotNull(
        response.jsonPath().getString("dateOfBirth"), "dateOfBirth should be not null");
    Assertions.assertEquals("Russia", response.jsonPath().getString("birthCountry"));
  }

  @Test
  void testPersonGet() {
    String id = createTestPerson().jsonPath().getString("id");
    RestAssured.given()
        .header("Accept", "application/json")
        .when()
        .request("GET", "/persons/" + id)
        .then()
        .statusCode(200);
  }

  @Test
  void testPersonGetByFilter() {
    String id = createTestPerson().jsonPath().getString("id");
    Response passport =
        PassportApiControllerTest.createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);
    Response passport2 =
        PassportApiControllerTest.createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);
    RestAssured.given()
        .header("Accept", "application/json")
        .when()
        .request("GET", "/persons?passportNumber=" + passport.jsonPath().getString("number"))
        .then()
        .statusCode(200);
    RestAssured.given()
        .header("Accept", "application/json")
        .when()
        .request("GET", "/persons?passportNumber=" + 1)
        .then()
        .statusCode(404);
  }

  @Test
  void testPersonPut() {
    String id = createTestPerson().jsonPath().getString("id");
    LocalDate newDateOfBirth =
        DataGenerator.getCurrentDate()
            .minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2)));
    Response response =
        RestAssured.given()
            .header("Accept", "application/json")
            .header("Content-type", "application/json")
            .and()
            .body(new UpdatePersonRequest("John", "Smith", newDateOfBirth, "Serbia"))
            .when()
            .put("/persons/" + id)
            .then()
            .extract()
            .response();

    Assertions.assertEquals(202, response.statusCode());
    Assertions.assertEquals("John", response.jsonPath().getString("firstName"));
    Assertions.assertEquals("Smith", response.jsonPath().getString("lastName"));
  }

  @Test
  void testPersonDelete() {
    String id = createTestPerson().jsonPath().getString("id");
    RestAssured.given()
        .header("Accept", "application/json")
        .when()
        .request("DELETE", "/persons/" + id)
        .then()
        .statusCode(204);
  }

  @Test
  void testPassportPost() {
    String id = createTestPerson().jsonPath().getString("id");
    Response response =
        PassportApiControllerTest.createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);

    Assertions.assertEquals(201, response.statusCode());
    Assertions.assertEquals("active", response.jsonPath().getString("status"));
  }

  @Test
  void testPassportGetByFilter() {
    String id = createTestPerson().jsonPath().getString("id");
    Response passport =
        PassportApiControllerTest.createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);
    Response passport1 =
        PassportApiControllerTest.createTestPassport(id, PassportType.SAILOR, Status.ACTIVE);
    Response passport2 =
        PassportApiControllerTest.createTestPassport(id, PassportType.INTERNATIONAL, Status.ACTIVE);
    Response passport3 =
        PassportApiControllerTest.createTestPassport(id, PassportType.SAILOR, Status.INACTIVE);

    Response passports =
        RestAssured.given()
            .header("Accept", "application/json")
            .when()
            .request("GET", "/persons/" + id + "/passports?status=active")
            .then()
            .extract()
            .response();
    Assertions.assertEquals(200, passports.statusCode());
    Response passports2 =
        RestAssured.given()
            .header("Accept", "application/json")
            .when()
            .request("GET", "/persons/" + id + "/passports?status=inActive")
            .then()
            .extract()
            .response();
    Assertions.assertEquals(200, passports2.statusCode());
    Response passports3 =
        RestAssured.given()
            .header("Accept", "application/json")
            .when()
            .request("GET", "/persons/" + id + "/passports?startDate=2017-08-04")
            .then()
            .extract()
            .response();
    Response passports4 =
        RestAssured.given()
            .header("Accept", "application/json")
            .when()
            .request("GET", "/persons/" + id + "/passports?endDate=2017-08-04")
            .then()
            .extract()
            .response();
    Assertions.assertEquals(200, passports2.statusCode());
  }

  @Test
  void testPassportPut() {
    String id = createTestPerson().jsonPath().getString("id");
    Response createResponse =
        PassportApiControllerTest.createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);

    Assertions.assertEquals(201, createResponse.statusCode());
    Assertions.assertEquals("active", createResponse.jsonPath().getString("status"));

    Response errorResponse =
        RestAssured.given()
            .header("Accept", "application/json")
            .header("Content-type", "application/json")
            .and()
            .body(
                new UpdatePassportRequest(
                    PassportType.SAILOR,
                    117733L,
                    DataGenerator.getCurrentDate(),
                    DataGenerator.getCurrentDate().minusYears(2),
                    "123-132",
                    Status.ACTIVE))
            .when()
            .put("/persons/" + id + "/passports/" + createResponse.jsonPath().getString("id"))
            .then()
            .extract()
            .response();

    Assertions.assertEquals(422, errorResponse.statusCode());
  }

  @Test
  void testPassportDeactivate() {
    String id = createTestPerson().jsonPath().getString("id");
    Response createResponse =
        PassportApiControllerTest.createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);

    Assertions.assertEquals(201, createResponse.statusCode());
    Assertions.assertEquals("active", createResponse.jsonPath().getString("status"));

    Response errorResponse =
        RestAssured.given()
            .header("Accept", "application/json")
            .header("Content-type", "application/json")
            .when()
            .post("/passports/" + createResponse.jsonPath().getString("id") + "/deactivate")
            .then()
            .extract()
            .response();

    Assertions.assertEquals(200, errorResponse.statusCode());
    Assertions.assertNotEquals(createResponse, errorResponse);
  }
}
