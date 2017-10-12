package com.exercise.rest;

import com.exercise.model.Sensor;
import com.exercise.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping("/sensors")
@Controller
public class SensorResource {

    @Autowired
    private SensorService sensorService;

    @RequestMapping
    ResponseEntity getSensors() {
        Collection<Sensor> sensors = sensorService.getSensors();
        return ResponseEntity.ok(sensors);
    }


    @PostMapping
    ResponseEntity addSensor(@RequestBody Sensor sensor) {
        sensorService.addSensor(sensor);
        return ResponseEntity.ok().build();
    }

}
