package com.exercise.rest;

import com.exercise.model.Sensor;
import com.exercise.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RequestMapping("/sensors")
@Controller
public class SensorResource {
    private static final Logger LOG = LoggerFactory.getLogger(SensorResource.class);

    @Autowired
    private SensorService sensorService;

    @RequestMapping
    ResponseEntity getSensors() {
        LOG.info("Executing action get sensors.");
        Collection<Sensor> sensors = sensorService.getSensors();
        return ResponseEntity.ok(sensors);
    }

    @PostMapping
    ResponseEntity addSensor(@Valid @RequestBody Sensor sensor) {
        LOG.info("Executing action add sensor.");
        sensorService.addSensor(sensor);
        return ResponseEntity.ok(sensor);
    }
}
