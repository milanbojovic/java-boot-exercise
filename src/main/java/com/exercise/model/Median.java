package com.exercise.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class Median {

    @NotNull
    private long sensorId;

    @NotNull
    private BigDecimal value;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp timestamp;

    public Median() {
    }

    public Median(long sensorId, BigDecimal value, Timestamp timestamp) {
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public long getSensorId() {
        return sensorId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Median{" +
                "sensorId=" + sensorId +
                ", value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Median)) return false;
        Median median = (Median) o;
        return sensorId == median.sensorId &&
                Objects.equals(value, median.value) &&
                Objects.equals(timestamp, median.timestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sensorId, value, timestamp);
    }
}
