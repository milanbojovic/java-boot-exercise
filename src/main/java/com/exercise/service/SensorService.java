package com.exercise.service;

import com.exercise.exception.service.AlreadyPresentException;
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
q
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
    public void addSensor(Sensor sensor) throws AlreadyPresentException {
        LOG.debug("Executing action add sensor for " + sensor);

        if (findSensor(sensor.getSensorId()) == null) {
            LOG.debug("Sensor name uniquity check PASSED, proceeding with addSensor action");
            SensorRecord sensorRecord = create.newRecord(SENSOR);
            sensorRecord.setSensorPublicId(sensor.getSensorId());
            sensorRecord.store();
            LOG.debug(sensor.getSensorId() + "] successfully added.");
        } else {
            throw new AlreadyPresentException("Sensor name uniquity check FAILED, Sensor with id: \" " + sensor.getSensorId() + " \" is already present in database.");
        }
    }

    protected Sensor findSensor(String id) {
        LOG.debug("Fetching sensor from database for id " + id);
        return create.selectFrom(SENSOR).where(SENSOR.SENSOR_PUBLIC_ID.eq(id)).fetchOne(record -> new Sensor(record.getSensorPublicId()));
    }

    protected Sensor findSensor(long id) {
        LOG.debug("Fetching sensor from database for id " + id);
        return create.selectFrom(SENSOR).where(SENSOR.ID.eq(id)).fetchOne(record -> new Sensor(record.getSensorPublicId()));
    }
}
