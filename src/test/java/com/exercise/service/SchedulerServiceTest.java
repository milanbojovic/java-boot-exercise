package com.exercise.service;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SchedulerServiceTest {
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;

    @LocalServerPort
    private int port;

    @Autowired
    private MedianCalculatorService medianCalculatorService;

    @Autowired
    private MedianService medianService;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void calculateAllMedians_AssertAddedNewMedians_True() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int numberOfMediansForSensorOneBeforeTest = medianService.getMedians(ONE).size();
        int numberOfMediansForSensorTwoBeforeTest = medianService.getMedians(TWO).size();
        int numberOfMediansForSensorThreeBeforeTest = medianService.getMedians(THREE).size();

        medianCalculatorService.calculateAllMedians(df.parse("2018-01-15 01:40:00.123000000").getTime());

        assertEquals(numberOfMediansForSensorOneBeforeTest, medianService.getMedians(ONE).size());
        assertEquals(numberOfMediansForSensorTwoBeforeTest + 1, medianService.getMedians(TWO).size());
        assertEquals(numberOfMediansForSensorThreeBeforeTest + 1, medianService.getMedians(THREE).size());
    }
}