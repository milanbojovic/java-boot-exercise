package com.exercise.rest;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MeasurementResourceIntegrationTest {

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void should_return_preconfigured_measurements() {

        String jsonMeasurementStr = "{\"sensorId\":1,\"value\":5,\"timestamp\":\"2018-01-11 10:00:19\"}";
        given().
                contentType(JSON).
                body(jsonMeasurementStr).
                when().
                post("/measurements").
                then().
                statusCode(OK.value()).and()
                .body(equalTo(jsonMeasurementStr));

        when().
                get("/measurements").
                then().
                statusCode(OK.value()).
                body(containsString(jsonMeasurementStr));
    }

    @Test
    public void should_add_new_measurement() {
        // add measurement
        String jsonMeasurementStr = "{\"sensorId\":1,\"value\":5,\"timestamp\":\"2018-01-06 17:25:19\"}";
        given().
                contentType(JSON).
                body(jsonMeasurementStr).
                when().
                post("/measurements").
                then().
                statusCode(OK.value()).and()
                .body(equalTo(jsonMeasurementStr));

        // find new measurement within list
        when().
                get("/measurements").
                then().
                statusCode(OK.value()).
                body(containsString(jsonMeasurementStr));
    }

    @Test
    public void should_not_add_new_measurement() {
        String jsonMeasurementStr = "{\"sensorId\":1,\"value\":2,\"timestamp\":\"2018-01-06 10:00:00\"}";
        // add measurement
        given().
                contentType(JSON).
                body(jsonMeasurementStr).
                when().
                post("/measurements").
                then().
                statusCode(OK.value()).and()
                .body(equalTo(jsonMeasurementStr));
        // try to add same measurement again
        given().
                contentType(JSON).
                body(jsonMeasurementStr).
                when().
                post("/measurements").
                then().
                statusCode(BAD_REQUEST.value()).and().
                body(containsString("Measurement already exists for provided timestamp"));
    }
}
