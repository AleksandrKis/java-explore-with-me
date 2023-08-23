package ru.practicum.exceptions;

public class TwoHoursBeforeException extends RuntimeException {
    public TwoHoursBeforeException(String message) {
        super(message);
    }
}
