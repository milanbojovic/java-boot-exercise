package com.exercise.service;

import com.exercise.generated.public_.tables.records.SensorRecord;
import com.exercise.model.Sensor;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.exercise.generated.public_.tables.Sensor.SENSOR;

@Service
public class SensorService {
    private static final Logger LOG = LoggerFactory.getLogger(SensorService.class);

    @Autowired
    private DSLContext create;

    public Collection<Sensor> getSensors() {
        LOG.debug("Executing action get sensors - fetching sensors from DB");
        return create.selectFrom(SENSOR).fetch(record -> new Sensor(record.getSensorPublicId()));
    }

    @Transactional
    public void addSensor(Sensor sensor) {
        LOG.debug("Executing action add sensor for id [" + sensor.getSensorId() + "]");

        if (findSensor(sensor.getSensorId()) == null) {
            LOG.debug("Sensor name uniquity check PASSED, proceeding with addSensor action");
            SensorRecord sensorRecord = create.newRecord(SENSOR);
            sensorRecord.setSensorPublicId(sensor.getSensorId());
            sensorRecord.store();
            LOG.debug("Sensor [" + sensor.getSensorId() + "] successfully added.");
        } else {
            LOG.error("ERROR - Sensor name uniquity check FAILED");
            throw new IllegalStateException("Error - sensor with id: \" " + sensor.getSensorId() + " \" is already present in database.");
        }
    }

    protected Sensor findSensor(String sensorId) {
        return getSensors().stream()
                .filter(sensorElement -> sensorElement.getSensorId().equals(sensorId))
                .findFirst()
                .orElse(null);
    }

}
