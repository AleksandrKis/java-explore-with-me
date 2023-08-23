package ru.practicum.exceptions;

public class WrongTimePeriodException extends RuntimeException {
    public WrongTimePeriodException(String message) {
        super(message);
    }
}