package com.passportoffice.test;

import static com.passportoffice.test.PersonApiControllerTest.createTestPerson;
import static io.restassured.RestAssured.*;

import com.passportoffice.Application;
import com.passportoffice.controller.PassportsApiController;
import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.enums.PassportType;
import com.passportoffice.enums.Status;
import com.passportoffice.utils.DataGenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
        String id = createTestPerson().jsonPath().getString("id");
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

    @Test
    public void testPassportPutOk(){
        String id = createTestPerson().jsonPath().getString("id");
        Response passportResponse = createTestPassport(id, PassportType.SAILOR, Status.INACTIVE);
        Response  response = given()
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .and()
                .body("{\n" +
                        "    \"type\": \"sailor\",\n" +
                        "    \"number\": 112233,\n" +
                        "    \"givenDate\": \"2003-12-05\",\n" +
                        "    \"expirationDate\": \"2025-12-05\",\n" +
                        "    \"departmentCode\": \"123-123\",\n" +
                        "    \"status\": \"active\"\n" +
                        "}")
                .when()
                .put("/passports/" + passportResponse.jsonPath().getString("id"))
                .then()
                .extract().response();

        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals("2025-12-05", response.jsonPath().getString("expirationDate"));
    }

    @Test
    public void testPassportPutProcessableEntity(){
        String id = createTestPerson().jsonPath().getString("id");
        Response passportResponse = createTestPassport(id, PassportType.SAILOR, Status.INACTIVE);
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .and()
                .body("{\n" +
                        "    \"type\": \"sailor\",\n" +
                        "    \"number\": 112233,\n" +
                        "    \"givenDate\": \"2003-12-05\",\n" +
                        "    \"expirationDate\": \"2022-12-05\",\n" + // in the past
                        "    \"departmentCode\": \"123-123\",\n" +
                        "    \"status\": \"active\"\n" +
                        "}")
                .when()
                .put("/passports/" + passportResponse.jsonPath().getString("id"))
                .then()
                .extract().response();

        Assert.assertEquals(422, response.statusCode());
    }

    @Test
    public void testPassportPutNotFound(){
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .and()
                .body("{\n" +
                        "  \"status\": \"inActive\" \n}")
                .when()
                .put("/passports/123")
                .then()
                .extract().response();

        Assert.assertEquals(404, response.statusCode());
    }

}
