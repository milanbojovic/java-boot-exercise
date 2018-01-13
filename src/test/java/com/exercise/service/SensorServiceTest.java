package com.exercise.service;

import com.exercise.exception.service.AlreadyPresentException;
import com.exercise.model.Sensor;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SensorServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private SensorService sensorService;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getSensors_ReturnPreconfiguredSensors_True() {
        Collection<Sensor> sensors = sensorService.getSensors();

        assertThat(sensors, Matchers.contains(
                hasProperty("sensorId", is("sensor-longstreet-45")),
                hasProperty("sensorId", is("sensor-bertastreet-21")),
                hasProperty("sensorId", is("sensor-werdstreet-2"))
        ));
    }

    @Test
    public void addSensor_addSensor_True() {
        Sensor sensor = new Sensor("new-street-sensor1");
        sensorService.addSensor(sensor);

        assertNotNull("Assert newly added sensor exists.", sensorService.findSensor("new-street-sensor1"));
    }

    @Test
    public void addSensor_addSensor_False() {
        Sensor sensor = new Sensor("new-street-sensor2");
        sensorService.addSensor(sensor);
        int sensorsCount = sensorService.getSensors().size();

        try {
            sensorService.addSensor(sensor);
        } catch (AlreadyPresentException e) {
        } finally {
            assertEquals(sensorsCount, sensorService.getSensors().size());
        }
    }

    @Test
    public void findSensorString_ReturnSensor_True() {
        Sensor sensor = sensorService.findSensor("sensor-longstreet-45");
        assertNotNull("Assert sensor exists", sensor);
    }

    @Test
    public void findSensorString_ReturnSensor_False() {
        Sensor sensor = sensorService.findSensor("sensor-non-existant");
        assertNull("Assert sensor doesn't exist", sensor);
    }

    @Test
    public void findSensorLong_ReturnSensor_True() {
        Sensor sensor = sensorService.findSensor(1);
        assertNotNull("Assert sensor exists", sensor);
    }

    @Test
    public void findSensorLong_ReturnSensor_False() {
        Sensor sensor = sensorService.findSensor(0);
        assertNull("Assert sensor doesn't exist", sensor);
    }

}