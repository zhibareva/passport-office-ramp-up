import com.passportoffice.Application;
import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.dto.request.CreatePersonRequest;
import com.passportoffice.model.Status;
import com.passportoffice.model.PassportType;
import com.passportoffice.utils.DataGenerator;
import com.passportoffice.utils.PassportNumberGenerator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class PassportApiControllerTest {

  private PassportNumberGenerator passportNumberGenerator;
  private DataGenerator dataGenerator;

  @BeforeEach
  void init() {
    RestAssured.baseURI = "http://localhost/v1/";
    RestAssured.port = 7070;
    passportNumberGenerator = new PassportNumberGenerator();
    dataGenerator = new DataGenerator();
  }

  public Response createTestPassport(
      String personId, PassportType passportType, Status status) {
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
        dataGenerator.getCurrentDate()
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
  void testPassportDelete() {
    String id = createTestPerson().jsonPath().getString("id");
    Response passportResponse = createTestPassport(id, PassportType.SAILOR, Status.ACTIVE);
    RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .when()
        .request(Method.DELETE, "/passports/" + passportResponse.jsonPath().getString("id"))
        .then()
        .statusCode(200);
  }

  @Test
  void testPassportGet() {
    String id = createTestPerson().jsonPath().getString("id");
    Response passportResponse = createTestPassport(id, PassportType.SAILOR, Status.ACTIVE);
    RestAssured.given()
        .header(HttpHeaders.ACCEPT, ContentType.JSON)
        .when()
        .request(Method.GET, "/passports/" + passportResponse.jsonPath().getString("id"))
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

  @Test
  void testPassportDeactivate() {
    String id = createTestPerson().jsonPath().getString("id");
    Response passportResponse = createTestPassport(id, PassportType.SAILOR, Status.ACTIVE);
    Response generatedPassport =
        RestAssured.given()
            .header(HttpHeaders.ACCEPT, ContentType.JSON)
            .when()
            .request(
                Method.POST,
                "/passports/" + passportResponse.jsonPath().getString("id") + "/deactivate")
            .then()
            .statusCode(200)
            .extract()
            .response();
    Assertions.assertNotNull(
        generatedPassport.jsonPath().getString("id"), "id should be not null");
    Assertions.assertEquals(passportResponse.jsonPath().getString("type"),
        generatedPassport.jsonPath().getString("type"),"type should be equals");
    Assertions.assertEquals(passportResponse.jsonPath().getString("departmentCode"),
        generatedPassport.jsonPath().getString("departmentCode"), "code should be equals");

  }
}
