package com.exercise.rest;

import com.exercise.exception.rest.ApiError;
import com.exercise.exception.service.ParameterValidationException;
import com.exercise.model.Median;
import com.exercise.service.MedianService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequestMapping("/medians")
@Controller
public class MedianResource {
    private static final Logger LOG = LoggerFactory.getLogger(MedianResource.class);

    @Autowired
    private MedianService medianService;

    @RequestMapping
    ResponseEntity getMedians(@Valid @RequestParam("sensorId") long id,
                              @RequestParam("startTimestamp") Optional<Timestamp> startTimestamp,
                              @RequestParam("endTimestamp") Optional<Timestamp> endTimestamp
    ) {
        if (startTimestamp.isPresent() && !endTimestamp.isPresent() ||
                !startTimestamp.isPresent() && endTimestamp.isPresent()) {
            ParameterValidationException ex = new ParameterValidationException("Even startTimestamp and endTimestamp parameters are optional they must be provided togather");
            ApiError apiError = new ApiError(BAD_REQUEST, "Parameter validation error", ex);
            LOG.error("ERROR - ", apiError);
            return apiError.buildResponseEntity();
        }

        Collection<Median> medians;
        if (startTimestamp.isPresent() && endTimestamp.isPresent()) {
            LOG.info("Executing action get all median noise levels for period [" + startTimestamp + "," + endTimestamp + "]");
            medians = medianService.getMediansForPeriod(id, startTimestamp.get(), endTimestamp.get());
        } else {
            LOG.info("Executing action get all median noise levels");
            medians = medianService.getMedians(id);
        }
        return ResponseEntity.ok(medians);
    }
}