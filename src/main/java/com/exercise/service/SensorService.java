package com.exercise.service;

import com.exercise.generated.public_.tables.records.SensorRecord;
import com.exercise.model.Sensor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static com.exercise.generated.public_.tables.Sensor.SENSOR;

@Service
public class SensorService {

    @Autowired
    private DSLContext create;

    public Collection<Sensor> getSensors() {
        return create.selectFrom(SENSOR).fetch(record -> new Sensor(record.getSensorPublicId()));
    }

    @Transactional
    public void addSensor(Sensor sensor) {
        SensorRecord sensorRecord = create.newRecord(SENSOR);
        sensorRecord.setSensorPublicId(sensor.getSensorId());
        sensorRecord.store();
    }

}
