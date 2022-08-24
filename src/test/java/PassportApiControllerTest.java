import com.passportoffice.Application;
import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import com.passportoffice.utils.DataGenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class PassportApiControllerTest {

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = "http://localhost/v1/";
    RestAssured.port = 7070;
  }

  //  @AfterAll
  //  static void cleanUp() throws IOException {
  //    Process process = Runtime.getRuntime().exec("netstat -ano | findstr :7070");
  //    Runtime.getRuntime().exec("taskkill /PID " + process.pid() + " /F");
  //  }

  public static Response createTestPassport(
      String personId, PassportType passportType, Status status) {
    return RestAssured.given()
        .header("Accept", "application/json")
        .header("Content-type", "application/json")
        .and()
        .body(
            new CreatePassportRequest(
                passportType,
                DataGenerator.generatePassportNumber(),
                DataGenerator.getCurrentDate(),
                "123-321",
                status))
        .when()
        .post("/persons/" + personId + "/passports")
        .then()
        .extract()
        .response();
  }

  @Test
  void testPassportGet() {
    String id = PersonApiControllerTest.createTestPerson().jsonPath().getString("id");
    Response passportResponse = createTestPassport(id, PassportType.SAILOR, Status.INACTIVE);
    RestAssured.given()
        .header("Accept", "application/json")
        .when()
        .request("GET", "/passports/" + passportResponse.jsonPath().getString("id"))
        .then()
        .statusCode(200);
  }

  @Test
  void testPassportGetNotFound() {
    RestAssured.given()
        .header("Accept", "application/json")
        .when()
        .request("GET", "/passports/123")
        .then()
        .statusCode(404);
    RestAssured.given()
        .header("Accept", "application/json")
        .when()
        .request("GET", "/passport/1")
        .then()
        .statusCode(500);
  }
}
