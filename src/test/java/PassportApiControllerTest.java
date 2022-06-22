import static io.restassured.RestAssured.*;

import com.passportoffice.Application;
import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.model.PassportType;
import com.passportoffice.model.Status;
import com.passportoffice.utils.DataGenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PassportApiControllerTest {

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost/v1/";
        RestAssured.port = 7070;
    }

    public static Response createTestPassport(String personId, PassportType passportType, Status status) {
        return given()
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .and()
                .body(new CreatePassportRequest(
                        passportType,
                        DataGenerator.generatePassportNumber(),
                        DataGenerator.getCurrentDate(),
                        "123-321",
                        status))
                .when()
                .post("/persons/" + personId + "/passports")
                .then()
                .extract().response();
    }

    @Test
    public void testPassportGet(){
        String id = PersonApiControllerTest.createTestPerson().jsonPath().getString("id");
        Response passportResponse = createTestPassport(id, PassportType.SAILOR, Status.INACTIVE);
        given().
                header("Accept","application/json").
        when().request("GET", "/passports/" + passportResponse.jsonPath().getString("id") ).then().statusCode(200);
    }

    @Test
    public void  testPassportGetNotFound(){
        given().
                header("Accept","application/json").
                when().request("GET", "/passports/123").then().statusCode(404);
        given().
                header("Accept","application/json").
                when().request("GET", "/passport/1").then().statusCode(404);
    }

}
