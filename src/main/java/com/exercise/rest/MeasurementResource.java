package com.exercise.rest;

import com.exercise.exception.rest.ApiError;
import com.exercise.exception.service.AlreadyPresentException;
import com.exercise.exception.service.ParameterValidationException;
import com.exercise.model.Measurement;
import com.exercise.service.MeasurementService;
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

@RequestMapping("/measurements")
@Controller
public class MeasurementResource {
    private static final Logger LOG = LoggerFactory.getLogger(MeasurementResource.class);

    @Autowired
    private MeasurementService measurementService;

    @RequestMapping
    ResponseEntity getMeasurements() {
        LOG.info("Executing action get measurements.");
        Collection<Measurement> measurements = measurementService.getMeasurements();
        return ResponseEntity.ok(measurements);
    }

    @PostMapping
    ResponseEntity addMeasurement(@Valid @RequestBody Measurement measurement) {
        LOG.info("Executing action add measurement.");

        try {
            measurementService.addMeasurement(measurement);
            return ResponseEntity.ok(measurement);
        } catch (ParameterValidationException e) {
            ApiError apiError = new ApiError(BAD_REQUEST, "Parameter validation error - ", e);
            LOG.error("ERROR - ", apiError);
            return buildResponseEntity(apiError);
        } catch (AlreadyPresentException e) {
            ApiError apiError = new ApiError(BAD_REQUEST, "Measurement already exists for provided timestamp", e);
            LOG.error("ERROR - ", apiError);
            return buildResponseEntity(apiError);
        }

    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}