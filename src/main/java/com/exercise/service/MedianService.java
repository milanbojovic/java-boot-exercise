package com.exercise.service;

import com.exercise.exception.service.AlreadyPresentException;
import com.exercise.exception.service.ParameterValidationException;
import com.exercise.generated.public_.tables.records.MedianRecord;
import com.exercise.model.Median;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;

import static com.exercise.generated.public_.tables.Median.MEDIAN;

@Service
public class MedianService {
    private static final Logger LOG = LoggerFactory.getLogger(MedianService.class);

    @Autowired
    private DSLContext create;

    @Autowired
    private SensorService sensorService;

    public Collection<Median> getMedians(long sensorId) {
        LOG.debug("Executing action get all medians - fetching median values from DB");
        return create.selectFrom(MEDIAN).where(MEDIAN.SENSOR_ID.eq(sensorId)).fetch(
                record -> new Median(record.getSensorId(), record.getValue(), record.getTimestamp()));
    }

    public Collection<Median> getMediansForPeriod(long sensorId, Timestamp start, Timestamp end) {
        LOG.debug("Executing action get median for period - fetching median values from DB");
        return create.selectFrom(MEDIAN).where(MEDIAN.SENSOR_ID.eq(sensorId)).and(MEDIAN.TIMESTAMP.between(start, end)).fetch(
                record -> new Median(record.getSensorId(), record.getValue(), record.getTimestamp()));
    }

    @Transactional
    public void addMedian(Median median) throws AlreadyPresentException {
        LOG.debug("Executing action add sensor for " + median);

        if (sensorService.findSensor(median.getSensorId()) == null) {
            throw new ParameterValidationException("Sensor with id: \" " + median.getSensorId() + " \" doesn't exist.");
        }

        if (median.getValue().intValue() < 0) {
            throw new ParameterValidationException("Median noise level can't be negative value.");
        }

        if (findMedian(median) == null) {
            LOG.debug("Median name uniquity check PASSED, proceeding with addMedian action");
            MedianRecord medianRecord = create.newRecord(MEDIAN);
            medianRecord.setSensorId(median.getSensorId());
            medianRecord.setValue(median.getValue());
            medianRecord.setTimestamp(median.getTimestamp());
            medianRecord.store();
            LOG.debug(median.getSensorId() + "] successfully added.");
        } else {
            throw new AlreadyPresentException("Median sensorId and timestamp uniquity check FAILED, " + median + " is already present in database.");
        }
    }

    protected Median findMedian(Median median) {
        LOG.debug("Fetching median from database " + median);
        return create.selectFrom(MEDIAN)
                .where(MEDIAN.SENSOR_ID.eq(median.getSensorId())
                        .and(MEDIAN.TIMESTAMP.eq(median.getTimestamp()))).fetchOne(
                        record -> new Median(record.getSensorId(), record.getValue(), record.getTimestamp())
                );
    }
}
