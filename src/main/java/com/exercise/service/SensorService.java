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
        SensorRecord sensorRecord = create.newRecord(SENSOR);
        sensorRecord.setSensorPublicId(sensor.getSensorId());
        sensorRecord.store();
    }

}
