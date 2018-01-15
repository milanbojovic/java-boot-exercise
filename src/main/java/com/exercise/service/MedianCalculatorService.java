package com.exercise.service;

//import com.exercise.model.Median;

import com.exercise.model.Measurement;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;


@Component
public class MedianCalculatorService {
    private static final Logger LOG = LoggerFactory.getLogger(MedianCalculatorService.class);

    @Autowired
    private SensorService sensorService;

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private MedianService medianService;

    private static final int period = 3600 * 1000; // milliseconds

    @Scheduled(fixedRate = period)
    public void run() {
        long currentTime = System.currentTimeMillis();
        LOG.info("Hourly calculation started at: " + new Timestamp(currentTime));
        calculateAllMedians(currentTime);
    }

    protected void calculateAllMedians(long currentTime) {
        sensorService.getSensors().forEach(
                sensor -> calculateSingleMedianForSensor(
                        sensorService.findSensorDBId(sensor.getSensorId()), getStartTimestamp(currentTime, period), getEndTimestamp(currentTime)
                )
        );
    }

    private void calculateSingleMedianForSensor(Long sensorId, Timestamp startTimestamp, Timestamp endTimestamp) {
        Collection<Measurement> sensorMeasurements = measurementService.getMeasurements()
                .stream()
                .filter(measurement -> measurement.getSensorId() == sensorId)
                .filter(measurement -> measurement.getTimestamp().after(startTimestamp))
                .filter(measurement -> measurement.getTimestamp().before(endTimestamp))
                .collect(Collectors.toList());
        if (!sensorMeasurements.isEmpty()) {
            double[] sensorMeasurementsDouble = sensorMeasurements
                    .stream()
                    .mapToDouble(measurement -> measurement.getValue().doubleValue()).toArray();
            medianService.addMedian(new com.exercise.model.Median(sensorId, BigDecimal.valueOf(getMedian(sensorMeasurementsDouble)), endTimestamp));
        }
    }

    private Timestamp getStartTimestamp(long currentTime, int period) {
        return new Timestamp(currentTime - period);
    }

    private Timestamp getEndTimestamp(long currentTime) {
        return new Timestamp(currentTime);
    }

    private double getMedian(double[] values) {
        Median median = new Median();
        double medianValue = median.evaluate(values);
        return medianValue;
    }
}
