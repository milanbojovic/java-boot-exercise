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

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MedianCalculatorResourceIntegrationTest {

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void should_return_all_preconfigured_medians() {
        String jsonMedianString1 =
                "{\"sensorId\":1,\"value\":12,\"timestamp\":\"2018-01-07 14:00:00\"}";
        String jsonMedianString2 =
                "{\"sensorId\":1,\"value\":2,\"timestamp\":\"2018-01-07 15:00:00\"}";
        String jsonMedianString3 =
                "{\"sensorId\":1,\"value\":6,\"timestamp\":\"2018-01-07 16:00:00\"}";
        when().
                get("/medians?sensorId=1").
                then().
                statusCode(OK.value()).
                body(containsString(jsonMedianString1));
        when().
                get("/medians?sensorId=1").
                then().
                statusCode(OK.value()).
                body(containsString(jsonMedianString2));
        when().
                get("/medians?sensorId=1").
                then().
                statusCode(OK.value()).
                body(containsString(jsonMedianString3));
    }

    @Test
    public void should_return_preconfigured_medians_for_period() {
        String jsonMedianString2 =
                "{\"sensorId\":1,\"value\":2,\"timestamp\":\"2018-01-07 15:00:00.123\"}";
        String jsonMedianString3 =
                "{\"sensorId\":1,\"value\":6,\"timestamp\":\"2018-01-07 16:00:00.123\"}";

        when().
                get("/medians?sensorId=1&startTimestamp=2018-01-07 15:00:00&endTimestamp=2018-01-07 16:01:00").
                then().
                statusCode(OK.value()).
                body(containsString(jsonMedianString2 + "," + jsonMedianString3));
    }
}
