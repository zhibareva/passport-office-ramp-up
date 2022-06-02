package com.passportoffice.test;

import com.passportoffice.Application;
import com.passportoffice.dto.request.CreatePassportRequest;
import com.passportoffice.dto.request.CreatePersonRequest;
import com.passportoffice.dto.request.UpdatePersonRequest;
import com.passportoffice.enums.PassportType;
import com.passportoffice.enums.Status;
import com.passportoffice.utils.DataGenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.passportoffice.test.PassportApiControllerTest.createTestPassport;
import static io.restassured.RestAssured.given;

@SpringBootTest(classes = Application.class)
public class PersonApiControllerTest {
    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost/v1/";
        RestAssured.port = 7070;
    }

    public static Response createTestPerson() {
        String firstName = DataGenerator.getName("firstName");
        String lastName = DataGenerator.getName("lastName");
        LocalDate dateOfBirth = DataGenerator.getCurrentDate().minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2)));

        return given()
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .and()
                .body(new CreatePersonRequest(firstName, lastName, dateOfBirth, "Russia"))
                .when()
                .post("/persons")
                .then()
                .extract().response();
    }

    @Test
    public void testPersonPost() {
        String firstName = DataGenerator.getName("firstName");
        String lastName = DataGenerator.getName("lastName");
        LocalDate dateOfBirth = DataGenerator.getCurrentDate().minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2)));

        Response response = createTestPerson();

        Assert.assertEquals(200, response.statusCode());
        Assert.assertNotNull("Id should be not null", response.jsonPath().getString("id"));
        Assert.assertEquals(firstName, response.jsonPath().getString("firstName"));
        Assert.assertEquals(lastName, response.jsonPath().getString("lastName"));
        Assert.assertEquals(dateOfBirth.toString(), response.jsonPath().getString("dateOfBirth"));
        Assert.assertEquals("Russia", response.jsonPath().getString("birthCountry"));
    }

    @Test
    public void testPersonGet() {
        String id = createTestPerson().jsonPath().getString("id");
        given().
                header("Accept", "application/json").
                when().request("GET", "/persons/" + id).then().statusCode(200);
    }

    @Test
    public void testPersonGetByFilter() {
        String id = createTestPerson().jsonPath().getString("id");
        Response passport = createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);
        Response passport2 = createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);
        given().
                header("Accept", "application/json").
                when().request("GET", "/persons?passportNumber=" + passport.jsonPath().getString("number")).then().statusCode(200);
        given().
                header("Accept", "application/json").
                when().request("GET", "/persons?passportNumber=" + 1).then().statusCode(404);
    }

    @Test
    public void testPersonPut() {
        String id = createTestPerson().jsonPath().getString("id");
        LocalDate newDateOfBirth = DataGenerator.getCurrentDate().minusYears(Long.parseLong(RandomStringUtils.randomNumeric(2)));
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .and()
                .body(new UpdatePersonRequest("John", "Smith", newDateOfBirth, "Serbia"))
                .when()
                .put("/persons/" + id)
                .then()
                .extract().response();

        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals("John", response.jsonPath().getString("firstName"));
        Assert.assertEquals("Smith", response.jsonPath().getString("lastName"));
    }

    @Test
    public void testPersonDelete() {
        String id = createTestPerson().jsonPath().getString("id");
        given().
                header("Accept", "application/json").
                when().request("DELETE", "/persons/" + id).then().statusCode(200);
    }

    @Test
    public void testPassportPost() {
        String id = createTestPerson().jsonPath().getString("id");
        Response response = createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);

        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals("active", response.jsonPath().getString("status"));
    }

    @Test
    public void testPassportGetByFilter() {
        String id = createTestPerson().jsonPath().getString("id");
        Response passport = createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);
        Response passport1 = createTestPassport(id, PassportType.SAILOR, Status.ACTIVE);
        Response passport2 = createTestPassport(id, PassportType.INTERNATIONAL, Status.ACTIVE);
        Response passport3 = createTestPassport(id, PassportType.SAILOR, Status.INACTIVE);

        Response passports = given().
                header("Accept", "application/json").
                when().request("GET", "/persons/" + id + "/passports").then()
                .extract().response();
    }



    @Test
    public void testPassportPut() {
        String id = createTestPerson().jsonPath().getString("id");
        Response createResponse = createTestPassport(id, PassportType.CITIZEN, Status.ACTIVE);

        Assert.assertEquals(200, createResponse.statusCode());
        Assert.assertEquals("active", createResponse.jsonPath().getString("status"));

        Response errorResponse = given()
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .and()
                .body("{\n" +
                        "    \"type\": \"sailor\",\n" +
                        "    \"number\": 117733,\n" +
                        "    \"givenDate\": \"2003-12-05\",\n" +
                        "    \"expirationDate\": \"2000-12-05\",\n" + // in the past
                        "    \"departmentCode\": \"123-123\",\n" +
                        "    \"status\": \"active\"\n" +
                        "}")
                .when()
                .post("/persons/" + id + "/passports")
                .then()
                .extract().response();

        Assert.assertEquals(422, errorResponse.statusCode());
    }

}
