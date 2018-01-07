package com.exercise.model;

import javax.validation.constraints.NotNull;

public class Sensor {

    @NotNull
    private String sensorId;

    public Sensor() {
    }

    public Sensor(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorId() {
        return sensorId;
    }
}
