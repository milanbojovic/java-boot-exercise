package com.exercise.service;

import com.exercise.exception.service.AlreadyPresentException;
import com.exercise.exception.service.ParameterValidationException;
import com.exercise.generated.public_.tables.records.MeasurementRecord;
import com.exercise.model.Measurement;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.exercise.generated.public_.tables.Measurement.MEASUREMENT;

@Service
public class MeasurementService {
    private static final Logger LOG = LoggerFactory.getLogger(MeasurementService.class);

    @Autowired
    private DSLContext create;

    @Autowired
    private SensorService sensorService;

    public Collection<Measurement> getMeasurements() {
        LOG.debug("Executing action get measurements - fetching all measurements from DB");
        return create.selectFrom(MEASUREMENT).fetch(
                record -> new Measurement(record.getSensorId(), record.getValue(), record.getTimestamp())
        );
    }

    @Transactional
    public void addMeasurement(Measurement measurement) throws ParameterValidationException, AlreadyPresentException {
        LOG.debug("Executing action add measurement for " + measurement);
        if (sensorService.findSensor(measurement.getSensorId()) == null) {
            throw new ParameterValidationException("Sensor with id: \" " + measurement.getSensorId() + " \" doesn't exist.");
        }

        if (measurement.getValue().intValue() < 0) {
            throw new ParameterValidationException("Noise level can't be negative value.");
        }

        if (findMeasurement(measurement) == null) {
            LOG.debug("Measurement name and timestamp uniquity check PASSED, proceeding with addMeasurement action");
            MeasurementRecord measurementRecord = create.newRecord(MEASUREMENT);
            measurementRecord.setSensorId(measurement.getSensorId());
            measurementRecord.setValue(measurement.getValue());
            measurementRecord.setTimestamp(measurement.getTimestamp());
            measurementRecord.store();
            LOG.debug(measurement + " successfully added.");
        } else {
            throw new AlreadyPresentException("Measurement name and timestamp uniquity check FAILED. " + measurement + " is already present in database.");
        }
    }

    protected Measurement findMeasurement(Measurement measurement) {
        return getMeasurements().stream()
                .filter(measurementElement -> measurementElement.equals(measurement))
                .findFirst()
                .orElse(null);
    }
}
