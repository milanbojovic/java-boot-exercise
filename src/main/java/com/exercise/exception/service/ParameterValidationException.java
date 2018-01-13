package com.exercise.exception.service;

public class ParameterValidationException extends RuntimeException {
    public ParameterValidationException(String message) {
        super(message);
    }
}
