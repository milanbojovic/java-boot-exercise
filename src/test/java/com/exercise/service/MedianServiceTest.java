package com.exercise.service;

import com.exercise.exception.service.ParameterValidationException;
import com.exercise.model.Median;
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
public class MedianServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MedianService medianService;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void getMedians_ReturnPreconfiguredMedians_True() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp ts1 = new Timestamp(df.parse("2018-01-07 15:00:00.123").getTime());
        Timestamp ts2 = new Timestamp(df.parse("2018-01-07 16:00:00.123").getTime());
        Timestamp ts3 = new Timestamp(df.parse("2018-01-07 17:00:00.123").getTime());

        Median m1 = new Median(1, BigDecimal.valueOf(12.0), ts1);
        Median m2 = new Median(1, BigDecimal.valueOf(2.0), ts2);
        Median m3 = new Median(1, BigDecimal.valueOf(6.0), ts3);

        Collection<Median> medians = medianService.getMedians(1);
        assertThat(medians, Matchers.contains(m1, m2, m3));
    }

    @Test
    public void getMediansForPeriod_ReturnPreconfiguredMediansForPeriod_True() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp start = new Timestamp(df.parse("2018-01-07 15:00:00.123").getTime());
        Timestamp end = new Timestamp(df.parse("2018-01-07 16:00:00.123").getTime());

        Median m1 = new Median(1, BigDecimal.valueOf(12.0), start);
        Median m2 = new Median(1, BigDecimal.valueOf(2.0), end);

        Collection<Median> medians = medianService.getMediansForPeriod(1, start, end);

        assertThat(medians, Matchers.contains(m1, m2));
        assertEquals(2, medians.size());
    }

    @Test
    public void addMedian_ReturnAddedMedian_True() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp ts = new Timestamp(df.parse("2018-01-04 15:00:00.123").getTime());
        Median median = new Median(1, BigDecimal.valueOf(2), ts);

        medianService.addMedian(median);

        assertEquals(median, medianService.findMedian(median));
    }

    @Test
    public void addMedian_returnAddMedian_False() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp ts = new Timestamp(df.parse("2018-01-07 18:00:00.123").getTime());
        Median median = new Median(1, BigDecimal.valueOf(-10), ts);

        try {
            medianService.addMedian(median);
        } catch (ParameterValidationException e) {
        } finally {
            assertNull("Assert that new median doesn't exist in DB", medianService.findMedian(median));
        }
    }

    @Test
    public void findMedian_ReturnMedian_True() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp ts = new Timestamp(df.parse("2018-01-07 15:00:00.123").getTime());
        Median median = new Median(1, BigDecimal.valueOf(12), ts);

        assertNotNull("Assert median found in DB", medianService.findMedian(median));
    }

    @Test
    public void findMedian_ReturnMedian_False() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp ts = new Timestamp(df.parse("2011-01-07 13:00:00.123").getTime());
        Median median = new Median(1, BigDecimal.valueOf(200), ts);

        assertNull("Assert median doesn't exist in DB", medianService.findMedian(median));
    }
}