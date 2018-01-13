package com.exercise.exception.service;

public class AlreadyPresentException extends RuntimeException {
    public AlreadyPresentException(String message) {
        super(message);
    }
}
