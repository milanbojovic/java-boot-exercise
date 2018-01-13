package com.exercise.service;

import com.exercise.exception.service.ParameterValidationException;
import com.exercise.model.Measurement;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MeasurementServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MeasurementService measurementService;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void getMeasurements_ReturnPreconfiguredMeasurements_True() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp ts1 = new Timestamp(df.parse("2018-01-06 15:52:32").getTime());
        Measurement measurement = new Measurement(1, BigDecimal.valueOf(2), ts1);

        measurementService.addMeasurement(measurement);
        Collection<Measurement> measurements = measurementService.getMeasurements();

        assertTrue(measurements.contains(measurement));
    }

    @Test
    public void addMeasurement_addMeasurement_True() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp ts = new Timestamp(df.parse("2018-01-10 10:10:10").getTime());
        Measurement measurement = new Measurement(2, BigDecimal.valueOf(50), ts);
        measurementService.addMeasurement(measurement);

        assertEquals(measurement, measurementService.findMeasurement(measurement));
    }

    @Test
    public void addMeasurement_addMeasurement_False() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp ts = new Timestamp(df.parse("2018-01-10 10:10:10").getTime());
        Measurement measurement = new Measurement(2, BigDecimal.valueOf(-50), ts);

        try {
            measurementService.addMeasurement(measurement);
        } catch (ParameterValidationException e) {
        } finally {
            assertNull("Assert that new measurement doesn't exist in DB", measurementService.findMeasurement(measurement));
        }
    }

    @Test
    public void findMeasurement_ReturnMeasurement_True() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp ts = new Timestamp(df.parse("2018-01-10 10:10:10").getTime());
        Measurement measurement = new Measurement(2, BigDecimal.valueOf(50), ts);

        assertNotNull("Assert measurement found in DB", measurementService.findMeasurement(measurement));
    }

    @Test
    public void findMeasurement_ReturnMeasurement_False() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp ts = new Timestamp(df.parse("2018-01-06 11:11:11").getTime());
        Measurement m = new Measurement(1, BigDecimal.valueOf(200), ts);

        assertNull("Assert measurement doesn't exist in DB", measurementService.findMeasurement(m));
    }
}