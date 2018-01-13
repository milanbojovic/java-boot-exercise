package com.exercise.rest;

import com.exercise.exception.rest.ApiError;
import com.exercise.model.Sensor;
import com.exercise.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Collection;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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

        try {
            sensorService.addSensor(sensor);
            return ResponseEntity.ok(sensor);
        } catch (Exception e) {
            ApiError apiError = new ApiError(BAD_REQUEST, "Sensor already exists", e);
            LOG.error("ERROR - ", apiError);
            return buildResponseEntity(apiError);
        }
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
