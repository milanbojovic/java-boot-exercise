package com.exercise.rest;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SensorResourceIntegrationTest {

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void should_return_preconfigured_sensors() {
        when().
                get("/sensors").
        then().
                statusCode(OK.value()).
                body("sensorId", hasItems("sensor-longstreet-45", "sensor-bertastreet-21", "sensor-werdstreet-2"));
    }

    @Test
    public void should_add_new_sensor() {
        // add sensor
        given().
                contentType(JSON).
                body("{ \"sensorId\": \"sensor-teststreet-1\" }").
        when().
                post("/sensors").

        then().
                statusCode(OK.value());

        // get sensors
        when().
                get("/sensors").
        then().
                statusCode(OK.value()).
                body("sensorId", hasItems("sensor-teststreet-1"));
    }

}
